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
@RequestMapping(value = "/userList")
@SessionAttributes("user")
public class UserListController {
	private static final Logger logger = LoggerFactory.getLogger(UserListController.class);
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/primaryModalView")
	public String primaryModalView(Long id, String modifyModel, Model model) throws Exception {
		logger.debug("primaryModalView request:" + id + ",model:" + model);
		if (id != null) {
			UserInfo user = userService.selectByPrimaryKey(id);
			model.addAttribute("modifyInfo", user);
		} 

		model.addAttribute("modifyModel", modifyModel);
		logger.debug("primaryModalView model : " + model);

		return "page/userList/ModifyModal";
	}

	@RequestMapping(value = "/userListView")
	public ModelAndView userListView(UserInfo userInfo, PageTools pageTools,
			@ModelAttribute("user") UserInfo user) {
		logger.debug("userListView request : " +user);
		
		if (!user.isAdmin()) {
			userInfo.setuId(user.getuId());
		}
		Long size = userService.selectPageTotalCount(userInfo);
		pageTools.setTotal(size);
		ModelAndView mv = new ModelAndView("page/userList/UserListView");
		userInfo.setPageTools(pageTools);
		mv.addObject("pageTools", pageTools);
		return mv;
	}

	@RequestMapping(value = "/loadPage")
	public ModelAndView loadPage(UserInfo userInfo, PageTools pageTools,
			@ModelAttribute("user") UserInfo user) throws Exception {
		logger.debug("loadPage User request:" + userInfo + " page info ===" + pageTools);
		
		if (!user.isAdmin()) {
			userInfo.setuId(user.getuId());
		}

		userInfo.setPageTools(pageTools);
		ModelAndView mv = new ModelAndView("page/userList/Content :: container-fluid");
		Long size = userService.selectPageTotalCount(userInfo);
		pageTools.setTotal(size);
		List<UserInfo> ciList = userService.selectPageByObjectForList(userInfo);
		logger.debug("loadPage User result list info =====:" + ciList);
		
		mv.addObject("resultList", ciList);
		mv.addObject("pageTools", pageTools);
		mv.addObject("queryObject", userInfo);

		return mv;
	}

	@RequestMapping(value="/userInfoEdit")
	@ResponseBody
	public Map<String,Object> userInfoEdit(UserInfo userInfo){
		logger.info("userInfoEdit request:{}",userInfo);
		int num = userService.updateByPrimaryKeySelective(userInfo);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", true);
		map.put("editNumber", num);
		return map;
	}
	
	@RequestMapping(value="/userInfoAdd")
	@ResponseBody
	public Map<String,Object> userInfoAdd(UserInfo userInfo) throws Exception{
		logger.info("userInfoAdd request:{}",userInfo);
		userInfo.setPassword("123");
		int num = userService.insertSelective(userInfo);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", true);
		map.put("addNumber", num);
		return map;
	}
	
	@RequestMapping(value="/pwdReset")
	@ResponseBody
	public Map<String,Object> pwdReset(UserInfo userInfo) throws Exception{
		logger.info("pwdReset request:{}",userInfo);
		userInfo.setNewPwd("123");
		userService.updateByPrimaryKeySelective(userInfo);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", true);
		return map;
	}
	
	
}
