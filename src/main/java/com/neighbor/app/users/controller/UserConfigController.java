package com.neighbor.app.users.controller;

import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/userConfig")
@SessionAttributes("user")
public class UserConfigController {
	private static final Logger logger = LoggerFactory.getLogger(UserConfigController.class);
	@Autowired
	private UserService userService; 

	@RequestMapping(value = "/userConfigShow.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult userInfoShow(@ModelAttribute("user") UserInfo user) {
		logger.info("userView request : " +user);
		UserInfo resultUser = userService.selectByPrimaryKey(user.getId());
		
		ResponseResult result = new ResponseResult();
		result.addBody("userConfig", resultUser);
		return result;
	}

	@RequestMapping(value="/userEdit.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult userEdit(UserInfo userInfo,@ModelAttribute("user") UserInfo user){
		logger.info("userEdit request:{}",userInfo);
		userInfo.setId(user.getId());
		userService.updateByPrimaryKeySelective(userInfo);
		return new ResponseResult();
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
	
}
