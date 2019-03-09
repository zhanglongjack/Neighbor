package com.neighbor.app.robot.controller;

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

import com.neighbor.app.robot.entity.RobotConfig;
import com.neighbor.app.robot.service.RobotConfigService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;

@Controller
@RequestMapping(value = "/robot")
@SessionAttributes("user")
public class RobotController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private RobotConfigService robotConfigService;

	@RequestMapping(value = "/primaryModalView.ser")
	public String primaryModalView(Integer id, String modifyModel, Model model) throws Exception {
		logger.debug("primaryModalView request:" + id + ",model:" + model);
		if (id != null) {
			RobotConfig robot = robotConfigService.selectByPrimaryKey(id);
			model.addAttribute("modifyInfo", robot);
		} 
 
		model.addAttribute("modifyModel", modifyModel);
		logger.debug("primaryModalView model : " + model);

		return "page/robot/ModifyModal";
	}

	@RequestMapping(value = "/pageView.ser")
	@ResponseBody
	public Map<String, Object> pageView(RobotConfig robot, PageTools pageTools,
			@ModelAttribute("user") UserInfo user) {
		logger.debug("robotView request : " +user);
		
		Long size = robotConfigService.selectPageTotalCount(robot);
		pageTools.setTotal(size);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("pageTools", pageTools);
		return result;
	}

	@RequestMapping(value = "/loadPage.ser")
	public ModelAndView loadPage(RobotConfig robot, PageTools pageTools,
			@ModelAttribute("user") UserInfo user) throws Exception {
		logger.debug("loadPage Robot request:" + robot + " page info ===" + pageTools);
	
		robot.setPageTools(pageTools);
		ModelAndView mv = new ModelAndView("page/robot/Content :: container-fluid");
		Long size = robotConfigService.selectPageTotalCount(robot);
		pageTools.setTotal(size);
		List<RobotConfig> ciList = robotConfigService.selectPageByObjectForList(robot);
		logger.debug("loadPage Robot result list info =====:" + ciList);
		
		mv.addObject("resultList", ciList);
		mv.addObject("pageTools", pageTools);
		mv.addObject("queryObject", robot);

		return mv;
	}

	@RequestMapping(value="/robotEdit.ser")
	@ResponseBody
	public Map<String,Object> userInfoEdit(RobotConfig robot){
		logger.info("userInfoEdit request:{}",robot);
		int num = robotConfigService.updateByPrimaryKeySelective(robot);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", true);
		map.put("editNumber", num);
		return map;
	}
	
	@RequestMapping(value="/robotAdd.ser")
	@ResponseBody
	public Map<String,Object> userInfoAdd(RobotConfig robot) throws Exception{
		logger.info("userInfoAdd request:{}",robot);
		int num = robotConfigService.insertSelective(robot);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", true);
		map.put("addNumber", num);
		return map;
	}


}
