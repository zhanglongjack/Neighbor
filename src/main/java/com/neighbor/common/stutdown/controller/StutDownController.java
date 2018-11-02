package com.neighbor.common.stutdown.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.neighbor.app.users.entity.UserInfo;

@Controller
public class StutDownController {
	private static final Logger logger = LoggerFactory.getLogger(StutDownController.class);
	
	@RequestMapping(value="/stutdown")
	public ModelAndView StutDown(HttpServletRequest request) throws Exception{
		logger.info("ordersView request");
		HttpSession session = request.getSession();
		UserInfo user = (UserInfo) session.getAttribute("user");
//		if(user!=null && user.isAdmin()){
//			System.exit(-1);
//		}
		throw new Exception("警报,有人想关闭系统");
	}
	
}
