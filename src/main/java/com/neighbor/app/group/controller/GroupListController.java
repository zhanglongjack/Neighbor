package com.neighbor.app.group.controller;

import com.neighbor.app.group.entity.Group;
import com.neighbor.app.group.service.GroupService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;
import com.neighbor.common.security.EncodeData;
import com.neighbor.common.util.PageTools;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/groupList")
@SessionAttributes("user")
public class GroupListController {
	private static final Logger logger = LoggerFactory.getLogger(GroupListController.class);
	@Autowired
	private GroupService groupService;

	@RequestMapping(value = "/primaryModalView.ser")
	public String primaryModalView(Long id, String modifyModel, Model model) throws Exception {
		logger.debug("primaryModalView request:" + id + ",model:" + model);
		if (id != null) {
			//Group group = groupService.s(id);
			//model.addAttribute("modifyInfo", user);
		} 

		model.addAttribute("modifyModel", modifyModel);
		logger.debug("primaryModalView model : " + model);

		return "page/groupList/ModifyModal";
	}

	@RequestMapping(value = "/pageView.ser")
	@ResponseBody
	public Map<String, Object> pageView(Group group, PageTools pageTools,
			@ModelAttribute("user") UserInfo user) {
		logger.debug("groupListView request : " +group);

		Long size = groupService.selectPageTotalCount(group);
		pageTools.setTotal(size);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("pageTools", pageTools);
		return result;
	}

	@RequestMapping(value = "/loadPage.ser")
	public ModelAndView loadPage(Group group, PageTools pageTools,
			@ModelAttribute("user") UserInfo user) throws Exception {
		logger.debug("loadPage User request:" + group + " page info ===" + pageTools);

		group.setPageTools(pageTools);
		ModelAndView mv = new ModelAndView("page/groupList/Content :: container-fluid");
		Long size = groupService.selectPageTotalCount(group);
		pageTools.setTotal(size);
		List<Group> ciList = groupService.selectPageByObjectForList(group);
		logger.debug("loadPage User result list info =====:" + ciList);
		
		mv.addObject("resultList", ciList);
		mv.addObject("pageTools", pageTools);
		mv.addObject("queryObject", group);

		return mv;
	}

	@RequestMapping(value="/addGroup.ser")
	@ResponseBody
	public Map<String,Object> addGroup(Group group,@ModelAttribute("user") UserInfo user) throws Exception{
		logger.info("addGroup request:{}",user);
		logger.info("addGroup request:{}",group);
		if(!user.isAdmin()){
			throw new Exception("很抱歉，您还没有权限~");
		}
		groupService.createGroup(group);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", true);
		map.put("addNumber", 1);
		return map;
	}

/*	@RequestMapping(value="/userInfoEdit.ser")
	@ResponseBody
	public Map<String,Object> userInfoEdit(UserInfo userInfo){
		logger.info("userInfoEdit request:{}",userInfo);
		int num = userService.updateByPrimaryKeySelective(userInfo);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", true);
		map.put("editNumber", num);
		return map;
	}
	

	
	@RequestMapping(value="/pwdReset.ser")
	@ResponseBody
	public Map<String,Object> pwdReset(UserInfo userInfo) throws Exception{
		logger.info("pwdReset request:{}",userInfo);
		userInfo.setUserPassword(EncodeData.encode("123456"));
		userService.updateByPrimaryKeySelective(userInfo);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", true);
		return map;
	}*/
	
	
}
