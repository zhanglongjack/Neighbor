package com.neighbor.app.users.controller;

import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.users.constants.UserContainer;
import com.neighbor.app.users.entity.UserConfig;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.common.exception.ParamsCheckException;
import com.neighbor.common.security.EncodeData;
import com.neighbor.common.sms.TencentSms;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.util.StringUtil;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@Validated
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private UserService userService;

	@Autowired
	private UserContainer userContainer;
	@Autowired
	private UserWalletService userWalletService;

	// mv.setViewName("forward:/login.html");
	// mv.setViewName("redirect:/login.html");
	// mv.addObject("message", "用户名或密码错误");
	@RequestMapping(value = "/accountLogin.ser", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult accountLogin(String phone, @NotNull(message = "密码不能为空") String password,
			HttpServletRequest request) throws Exception {
		ResponseResult result = login(phone, password);
		if (result.getErrorCode() == 0) {
			UserInfo user = (UserInfo) result.getBody().get("user");
			HttpSession session = request.getSession();
			user.setUserPassword("******");
			logger.info("登录成功:" + user);
			if(user.isAdmin()||user.isKF()){
				session.setAttribute("user", user);
			}else{
				result.setErrorCode(1);
				result.setErrorMessage("非系统管理员不允许登录");
				logger.info("登录失败:" + result);
			}
			
		}

		return result;
	}

	@RequestMapping(value = "/logout.ser")
	public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		session.invalidate();
		response.sendRedirect("/login.html");
	}

	@RequestMapping(value = "/accountLogin.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult login(String phone, @NotNull(message = "密码不能为空") String password) throws Exception {
		UserInfo user = userService.selectByUserPhone(phone);

		ResponseResult result = new ResponseResult();
		if (user == null || user.getUserPassword() == null || !EncodeData.matches(password, user.getUserPassword())) {
			logger.info("用户名或密码错误");
			throw new ParamsCheckException(ErrorCodeDesc.failed.getValue(), "用户名或密码错误!");
		}

		commonResultLogic(user, result);
		TencentSms.removeSMSCode(phone);

		logger.info("登录成功:{},result :{}", user, result);
		return result;
	}

	@RequestMapping(value = "/registerLogin.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult registerLogin(@NotNull(message = "手机号不能为空") String phone,
			@NotNull(message = "验证码不能为空") String verfiyCode, String onlyLogin, Long upUserId) throws Exception {

		if (StringUtil.isEmpty(onlyLogin) || !onlyLogin.equals("1")) {
			UserInfo userUp = userService.selectByPrimaryKey(upUserId);
			if (userUp == null) {
				logger.info("上级人员不存在......");
				throw new ParamsCheckException(ErrorCodeDesc.failed.getValue(), "上级人员不存在......");
			}
		}

		UserInfo user = userService.selectByUserPhone(phone);
		boolean isValid = verfiyCode.equals(TencentSms.smsCache.get(phone));
		ResponseResult result = new ResponseResult();

		if (user == null && isValid) {

			if (StringUtil.isNotEmpty(onlyLogin) && onlyLogin.equals("1")) {
				logger.info("APP只允许登录......");
				throw new ParamsCheckException(ErrorCodeDesc.failed.getValue(), "APP只允许登录......");
			}

			UserInfo record = new UserInfo();
			record.setUserPhoto("img/head-user2.png");
			record.setMobilePhone(phone);
			record.setUserAccount(phone);
			record.setUpUserId(upUserId);

			user = userService.builderUserInfo(record);

		} else if (!isValid) {
			logger.info("验证吗输入错误");
			throw new ParamsCheckException(ErrorCodeDesc.failed.getValue(), "验证码错误");
		} else if (user.getId().equals(upUserId)) {
			if (StringUtil.isEmpty(onlyLogin) || !onlyLogin.equals("1")) {
				logger.info("上级人员不能是本人......");
				throw new ParamsCheckException(ErrorCodeDesc.failed.getValue(), "上级人员不能是本人......");
			}
		}

		commonResultLogic(user, result);
		TencentSms.removeSMSCode(phone);
		logger.info("登录成功:{},result :{}", user, result);
		return result;
	}

	private void commonResultLogic(UserInfo user, ResponseResult result) throws Exception {
		user.setUserPassword(null);
		String token = UUID.randomUUID().toString();
		userContainer.put(token, user);

		result.addBody("user", user);
		result.addBody("token", token);
		UserWallet wallet = userWalletService.selectByPrimaryUserId(user.getId());

		result.addBody("wallet", wallet);
		UserConfig userConfig = userService.selectUserConfigByPrimaryKey(user.getId());
		if (userConfig == null) {
			Date currentTime = new Date();
			userConfig = new UserConfig();
			userConfig.setUserId(user.getId());
			userConfig.setCreateTime(currentTime);
			userConfig.setUpdateTime(currentTime);
			userConfig.setHaveShock(UserConfig.HaveShockDesc.haveShock.getValue());
			userConfig.setHaveVoice(UserConfig.HaveVoiceDesc.haveVoice.getValue());
			userService.insertUserConfigSelective(userConfig);
		}
		result.addBody("userConfig", userConfig);
	}

	@RequestMapping(value = "/sendSMS.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult sendSMS(@Length(message = "手机长度最少11位", min = 11) @NotNull(message = "手机号不能为空") String phone) {
		String code = TencentSms.createVerifyCode();
		logger.info("发送验证码:" + phone);
		TencentSms.smsSend(code, phone);
		return new ResponseResult();
	}

	@RequestMapping(value = "/logout.req")
	@ResponseBody
	public Map<String, Object> logout(HttpServletRequest request, String token) {
		logger.info("注销:" + token);
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		userContainer.userMap.remove(token);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("errorCode", 0);
		logger.info("注销结束:" + map);
		return map;
	}

}
