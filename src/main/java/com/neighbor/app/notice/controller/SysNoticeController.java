package com.neighbor.app.notice.controller;

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

import com.neighbor.app.notice.entity.SysNotice;
import com.neighbor.app.notice.service.SysNoticeService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;

@Controller
@RequestMapping(value = "/sysNotice")
@SessionAttributes("user")
public class SysNoticeController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private SysNoticeService sysNoticeService;

	@RequestMapping(value = "/primaryModalView.ser")
	public String primaryModalView(Long id, String modifyModel, Model model) throws Exception {
		logger.debug("primaryModalView request:" + id + ",model:" + model);
		if (id != null) {
			SysNotice paramData = sysNoticeService.selectByPrimaryKey(id);
			model.addAttribute("modifyInfo", paramData);
		}

		model.addAttribute("modifyModel", modifyModel);
		logger.debug("primaryModalView model : " + model);

		return "page/sysNotice/ModifyModal";
	}

	@RequestMapping(value = "/pageView.ser")
	@ResponseBody
	public Map<String, Object> pageView(SysNotice paramData, PageTools pageTools,
			@ModelAttribute("user") UserInfo user) {
		logger.debug("pageView request : " + paramData);

		Long size = sysNoticeService.selectPageTotalCount(paramData);
		pageTools.setTotal(size);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("pageTools", pageTools);
		return result;
	}

	@RequestMapping(value = "/loadPage.ser")
	public ModelAndView loadPage(SysNotice paramData, PageTools pageTools, @ModelAttribute("user") UserInfo user)
			throws Exception {
		logger.debug("loadPage request:" + paramData + " page info ===" + pageTools);

		ModelAndView mv = new ModelAndView("page/sysNotice/Content :: container-fluid"); 
		return mv;
	}

	@RequestMapping(value = "/loadPageAjax.ser")
	@ResponseBody
	public Map<String, Object> loadPage(SysNotice paramData, @ModelAttribute("user") UserInfo user) throws Exception {
		logger.debug("loadPage loadPageAjax request:{}", paramData);

		Long size = sysNoticeService.selectPageTotalCount(paramData);
		List<SysNotice> resultList = sysNoticeService.selectPageByObjectForList(paramData);
		logger.debug("loadPage loadPageAjax result list info =====:" + resultList);

		Map<String, Object> mv = new HashMap<String, Object>();
		mv.put("rows", resultList);
		mv.put("total", size);
		return mv;
	}

	@RequestMapping(value = "/sysNoticeEdit.ser")
	@ResponseBody
	public ResponseResult sysNoticeEdit(SysNotice paramData) {
		logger.info("sysNoticeEdit request:{}", paramData);
		sysNoticeService.updateByPrimaryKeySelective(paramData);
		return new ResponseResult();
	}

	@RequestMapping(value = "/sysNoticeAdd.ser")
	@ResponseBody
	public ResponseResult sysNoticeAdd(SysNotice paramData) throws Exception {
		logger.info("sysNoticeAdd request:{}", paramData);
		sysNoticeService.insert(paramData);
		return new ResponseResult();
	}

	@RequestMapping(value = "/showNotice.req")
	@ResponseBody
	public ResponseResult showNotice() throws Exception {
		logger.debug("showNotice request");
		SysNotice record = new SysNotice();
		record.setStatus(1);
		List<SysNotice> resultList = sysNoticeService.selectBySelective(record);
		ResponseResult result = new ResponseResult();
		result.getBody().put("resultList", resultList);
		return result;
	}

}
