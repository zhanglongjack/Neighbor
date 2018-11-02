package com.neighbor.app.users.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;


//@Controller
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private UserService userService;
//	mv.setViewName("forward:/login.html");
//	mv.setViewName("redirect:/login.html");
//	mv.addObject("message", "用户名或密码错误");
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> login(String phone,String password,HttpServletRequest request,ModelMap model){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo user  = userService.selectByUserPhone( phone );
		if(user==null || !user.getUserPassword().equals(password)){
			logger.info("用户名或密码错误");
			map.put("success", 0);
			map.put("message", "用户名或密码错误!"); 
			return map;
		}
		
		HttpSession session = request.getSession();
		user.setUserPassword(null);
		logger.info("登录成功:"+user);
		session.setAttribute("user", user);
//		session.setAttribute("levelMap", CustomerLevelContainer.levelMap);
//		session.setAttribute("levelList", CustomerLevelContainer.levelList);
//		session.setAttribute("orderStatusMap", OrderStatus.getValues());
//		session.setAttribute("paymentMethodMap", PaymentMethodStatus.paymentMethodMap);
//		session.setAttribute("consumeTypeMap", ConsumeType.consumeTypeMap);
//		session.setAttribute("staffLevelMap", StaffLevel.staffLevelMap);
		map.put("success", 1); 
		return map;
	}
	
	
	@RequestMapping(value="/logout")
	public ModelAndView logout(HttpServletRequest request){
		logger.info("注销");
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("forward:/login.html");
		return mv;
	}
	
}
