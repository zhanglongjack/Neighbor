package com.neighbor.app.users.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.neighbor.common.util.PageTools;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;

@Controller
@RequestMapping(value = "/user")
@SessionAttributes("user")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserService userService; 

	@RequestMapping(value = "/primaryModalView")
	public String primaryModalView(Long id, String modifyModel, Model model) throws Exception {
		logger.info("primaryModalView request:" + id + ",model:" + model);
		if (id != null) {
			UserInfo user = userService.selectByPrimaryKey(id);
			model.addAttribute("modifyInfo", user);
		} else {

		}

		model.addAttribute("modifyModel", modifyModel);
		logger.info("primaryModalView model : " + model);

		return "page/user/ModifyModal";
	}

//	@RequestMapping(value = "/userView")
//	public ModelAndView UserView(UserInfo userInfo, PageTools pageTools,
//			@ModelAttribute("user") UserInfo user) {
//		logger.info("userView request : " +user);
//		
//		if (!user.isAdmin()) {
//			userInfo.setuId(user.getuId());
//		}
//		Long size = userService.selectPageTotalCount(userInfo);
//		pageTools.setTotal(size);
//		ModelAndView mv = new ModelAndView("page/user/UserView");
//		userInfo.setPageTools(pageTools);
//		mv.addObject("pageTools", pageTools);
//		mv.addObject("pageTools", pageTools);
//		return mv;
//	}
//
//	@RequestMapping(value = "/loadPage")
//	public ModelAndView loadPage(UserInfo userInfo, PageTools pageTools,
//			@ModelAttribute("user") UserInfo user) throws Exception {
//		logger.info("loadPage User request:" + userInfo + " page info ===" + pageTools);
//		
//		if (!user.isAdmin()) {
//			userInfo.setuId(user.getuId());
//		}
//
//		userInfo.setPageTools(pageTools);
//		ModelAndView mv = new ModelAndView("page/user/UserContent :: container-fluid");
//		Long size = userService.selectPageTotalCount(userInfo);
//		pageTools.setTotal(size);
//		List<UserInfo> ciList = userService.selectPageByObjectForList(userInfo);
//		logger.info("loadPage User result list info =====:" + ciList);
//		
//		mv.addObject("resultList", ciList);
//		mv.addObject("pageTools", pageTools);
//		mv.addObject("queryObject", userInfo);
//
//		return mv;
//	}
	
	@RequestMapping(value="/userEdit")
	@ResponseBody
	public Map<String,Object> userEdit(UserInfo userInfo){
		logger.info("userEdit request:{}",userInfo);
		int num = userService.updateByPrimaryKeySelective(userInfo);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", true);
		map.put("editNumber", num);
		return map;
	}
	
	@RequestMapping(value="/checkPwd")
	@ResponseBody
	public Map<String,Object> checkPwd(Long id,String pwd){
		logger.info("checkPwd request:uid={},pwd={}",id,pwd);
		UserInfo user = userService.selectByPrimaryKey(id);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", user!=null&&user.getUserPassword().equals(pwd));
		return map;
	}
	
}
