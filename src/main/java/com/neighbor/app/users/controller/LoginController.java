package com.neighbor.app.users.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neighbor.app.users.constants.UserContainer;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;


@Controller
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserContainer userContainer;
	
	
//	mv.setViewName("forward:/login.html");
//	mv.setViewName("redirect:/login.html");
//	mv.addObject("message", "用户名或密码错误");
	@RequestMapping(value="/accountLogin.req",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> login(String phone,String password){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo user  = userService.selectByUserPhone( phone );
		if(user==null || !user.getUserPassword().equals(password)){
			logger.info("用户名或密码错误");
			map.put("errorCode", 1);
			map.put("message", "用户名或密码错误!"); 
			return map;
		}
		user.setUserPassword(null);
		
		String token = UUID.randomUUID().toString();
		map.put("errorCode", 0); 
		map.put("user", user); 
		map.put("token", token);
		userContainer.put(token, user);
		logger.info("登录成功:{},result :{}",user,map); 
		return map;
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
