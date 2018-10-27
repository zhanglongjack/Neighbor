package com.neighbor.app.users.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
//	public void login(String userName,String pwd,String code){
	
//	@RequestMapping(value="/index")
	public ModelAndView index(){
		logger.info("index request");
		ModelAndView mv = new ModelAndView("page/index"); 
		mv.addObject("test", "hello world !!!index");
		mv.addObject("date", new Date());
//		mv.setViewName("forward:/login.html");
		return mv;
	}
	
	@RequestMapping(value="/home")
	public ModelAndView home(){
		logger.info("home request");
		ModelAndView mv = new ModelAndView("page/Home");
		mv.addObject("test", "hello world !!!home");
		mv.addObject("date", new Date());
//		mv.setViewName("forward:/login.html");
		return mv;
	}
}
