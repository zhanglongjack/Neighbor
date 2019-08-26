package com.neighbor.app.users.controller;

import java.util.List;

import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.users.entity.UserConfig;
import com.neighbor.common.exception.ParamsCheckException;
import com.neighbor.common.security.EncodeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/user")
@SessionAttributes("user")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserService userService; 

	@RequestMapping(value = "/userInfoShow.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult userInfoShow(@ModelAttribute("user") UserInfo user) {
		logger.info("userView request : " +user);
		UserInfo resultUser = userService.selectByPrimaryKey(user.getId());
		
		ResponseResult result = new ResponseResult();
		result.addBody("user", resultUser);
		return result;
	}

	@RequestMapping(value="/userEdit.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult userEdit(UserInfo userInfo,@ModelAttribute("user") UserInfo user){
		logger.info("userEdit request:{}",userInfo);
		userInfo.setUserID(user.getId());
		userService.updateByPrimaryKeySelective(userInfo);
		return new ResponseResult();
	}

	@RequestMapping(value="/changePhone.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult changePhone(String phone,String verfiyCode,@ModelAttribute("user") UserInfo user,HttpServletRequest request){
		logger.info("changePhone request:phone = {} verfiyCode ={}",phone,verfiyCode);
		logger.info("changePhone user = {} ",user);
		ResponseResult result = userService.changePhone(phone,verfiyCode,user);
		if(ErrorCodeDesc.success.getValue()==result.getErrorCode()){
			user.setMobilePhone(phone);
			request.getSession().setAttribute("user",user);
			//更新手机号码
		}
		return result;
	}

	
	@RequestMapping(value="/checkPwd.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult checkPwd(Long id,String pwd){
		logger.info("checkPwd request:uid={},pwd={}",id,pwd);
		UserInfo user = userService.selectByPrimaryKey(id);
		
		ResponseResult result = new ResponseResult();
		result.addBody("user", user);
		return result;
	}
	
	@RequestMapping(value="/downUserInfo.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult downUserInfo(@ModelAttribute("user") UserInfo user,UserInfo queryUser,PageTools pageTools){
		logger.info("downUserInfo  request queryUser:{}",queryUser);
		if(queryUser.getUpUserId()==null){
			queryUser.setUpUserId(user.getId());
		}
		queryUser.setPageTools(pageTools);
		pageTools.setTotal(userService.selectPageTotalCount(queryUser));
		
		List<UserInfo> resultList = userService.selectPageByObjectForList(queryUser);
		
		ResponseResult result = new ResponseResult();
		result.addBody("resultList", resultList);
		result.addBody("pageTools", pageTools);
		return result;
	}


	@RequestMapping(value="/userPasswordEdit.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult userPasswordEdit(@ModelAttribute("user")UserInfo user) throws Exception{
		logger.info("userPasswordEdit request user :{} ",user);
		String userPassword = user.getUserPassword();
		if(StringUtils.isEmpty(userPassword)){
			logger.info("设置登录密码不能为空！");
			throw new ParamsCheckException(ErrorCodeDesc.failed.getValue(),"设置登录密码不能为空!");
		}

		user.setUserPassword(EncodeData.encode(userPassword));
		userService.userPasswordEdit(user);
		return new ResponseResult();
	}

	@RequestMapping(value="/userPasswordValid.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult userPasswordValid(@ModelAttribute("user")UserInfo user) throws Exception{
		logger.info("userPasswordValid request user :{} ",user);
		UserInfo userOld  = userService.selectByPrimaryKey(user.getId());
		if(!EncodeData.matches(user.getUserPassword(), userOld.getUserPassword())){
			logger.info("登录密码错误！");
			throw new ParamsCheckException(ErrorCodeDesc.failed.getValue(),"登录密码错误!");
		}
		return new ResponseResult();
	}

	@RequestMapping(value = "/userConfigSetting.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult userConfigSetting(@ModelAttribute("user") UserInfo user, UserConfig userConfig) throws Exception {
		logger.info("userConfigSetting request user >>>> " + user);
		userConfig.setUserId(user.getId());
		userService.userConfigSetting(userConfig);
		return new ResponseResult();
	}
	public static void main(String[] args) {
		System.out.println(EncodeData.encode("123456"));
	}
}
