package com.neighbor.app.group.controller;

import com.neighbor.app.group.entity.Group;
import com.neighbor.app.group.service.GroupService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
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

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/primaryModalView.ser")
	public String primaryModalView(Long id, String modifyModel, Model model) throws Exception {
		logger.debug("primaryModalView request:" + id + ",model:" + model);
		if (id != null) {
			Group query = new Group();
			query.setId(id);
			query.setPageTools(new PageTools());
			List<Group> groups = groupService.selectPageByObjectForList(query);
			Group group = null;
			if(groups!=null&&groups.size()>0){
				group = groups.get(0);
			}
			model.addAttribute("modifyInfo", group);
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

	
	@RequestMapping(value = "/groupSelectListModal.ser")
	public String groupSelectListModal(Integer id, Model model) throws Exception {
		logger.debug("GroupSelectListModal request:" + id + ",model:" + model);
		model.addAttribute("robotId", id);
		
		logger.debug("GroupSelectListModal model : " + model);
		
		return "page/robot/GroupSelectListModal";
	}

	@RequestMapping(value = "/robotOwnGroupPage.ser")
	@ResponseBody
	public Map<String,Object> robotOwnGroupPage(Group queryObject,@ModelAttribute("user") UserInfo user) throws Exception {
		logger.debug("robotOwnGroupPage request:" + queryObject + " user info ==="+user);
		
		Long size = groupService.selectPageTotalCountByRobotGroupRelation(queryObject);
		List<Group> ciList = groupService.selectPageByRobotGroupRelation(queryObject);
		logger.debug("robotOwnGroupPage result list info =====:" + ciList);
		
		Map<String,Object> mv = new HashMap<String,Object>();
		mv.put("rows", ciList);
		mv.put("total", size);
//		mv.put("queryObject", queryObject);
		return mv;
	}

	@RequestMapping(value="/addGroup.ser")
	@ResponseBody
	public ResponseResult addGroup(Group group, @ModelAttribute("user") UserInfo user) throws Exception{
		logger.info("addGroup request:{}",user);
		logger.info("addGroup request:{}",group);
		if(!user.isAdmin()){
			throw new Exception("很抱歉，您还没有权限~");
		}
		UserInfo userInfo = userService.selectByPrimaryKey(group.getGroupOwnerUserId());
		if(userInfo==null){
			throw new Exception("输入的群主编号不存在~");
		}
		groupService.createGroup(group);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", true);
		map.put("addNumber", 1);
		return new ResponseResult();
	}

	@RequestMapping(value="/updateGroup.ser")
	@ResponseBody
	public ResponseResult updateGroup(Group group,@ModelAttribute("user") UserInfo user) throws Exception{
		logger.info("updateGroup request:{}",user);
		logger.info("updateGroup request:{}",group);
		if(!user.isAdmin()){
			throw new Exception("很抱歉，您还没有权限~");
		}
		UserInfo userInfo = userService.selectByPrimaryKey(group.getGroupOwnerUserId());
		if(userInfo==null){
			throw new Exception("输入的群主编号不存在~");
		}
		groupService.updateGroup(group);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", true);
		map.put("addNumber", 1);
		return new ResponseResult();
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
