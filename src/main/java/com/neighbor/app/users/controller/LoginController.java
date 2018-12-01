package com.neighbor.app.users.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import com.neighbor.app.users.entity.UserConfig;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.users.constants.UserContainer;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.common.exception.ParamsCheckException;
import com.neighbor.common.security.EncodeData;
import com.neighbor.common.sms.TencentSms;
import com.neighbor.common.util.ResponseResult;


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
	
//	mv.setViewName("forward:/login.html");
//	mv.setViewName("redirect:/login.html");
//	mv.addObject("message", "用户名或密码错误");
	@RequestMapping(value="/accountLogin.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult login(String phone,@NotNull(message = "密码不能为空") String password) throws Exception{
		UserInfo user  = userService.selectByUserPhone( phone );
		
		ResponseResult result = new ResponseResult();
		if(user==null || user.getUserPassword()==null || !EncodeData.matches(password, user.getUserPassword())){
			logger.info("用户名或密码错误"); 
			throw new ParamsCheckException(ErrorCodeDesc.failed.getValue(),"用户名或密码错误!");
		}
		user.setUserPassword(null);
		
		String token = UUID.randomUUID().toString(); 
		userContainer.put(token, user);
		
		result.addBody("user", user);
		result.addBody("token", token);
		
		logger.info("登录成功:{},result :{}",user,result); 
		return result;
	}
	
	@RequestMapping(value="/registerLogin.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult registerLogin(@NotNull(message = "手机号不能为空")String phone,@NotNull(message = "验证码不能为空") String verfiyCode,Long upUserId) throws Exception{
		UserInfo user  = userService.selectByUserPhone( phone );
		boolean isValid = verfiyCode.equals(TencentSms.smsCache.get(phone));
		ResponseResult result = new ResponseResult();
		if(user==null && isValid){
			UserInfo record = new UserInfo();
			record.setMobilePhone(phone);
			record.setUserAccount(phone);
			record.setUpUserId(upUserId);

			user = userService.builderUserInfo(record);

		}else if(!isValid){
			logger.info("验证吗输入错误"); 
			throw new ParamsCheckException(ErrorCodeDesc.failed.getValue(),"验证码错误");
		}
        String token = UUID.randomUUID().toString();
        userContainer.put(token, user);

        UserWallet wallet = userWalletService.selectByPrimaryUserId(user.getId());

		result.addBody("user", user);
		result.addBody("wallet", wallet);
//		result.addBody("config", config);
		result.addBody("token", token);
		
		logger.info("登录成功:{},result :{}",user,result); 
		return result;
	}
	
	@RequestMapping(value="/sendSMS.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult sendSMS(@Length(message = "手机长度最少11位",min=11)@NotNull(message = "手机号不能为空")String phone){
//		String code = TencentSms.createVerifyCode()
		logger.info("发送验证码:"+phone);
		TencentSms.smsSend(null,phone);  
		return new ResponseResult();
	}
	
	@RequestMapping(value="/logout.req")
	@ResponseBody
	public Map<String,Object> logout(HttpServletRequest request,String token){
		logger.info("注销:"+token);
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		userContainer.userMap.remove(token);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("errorCode", 0); 
		logger.info("注销结束:"+map);
		return map;
	}
	
}
