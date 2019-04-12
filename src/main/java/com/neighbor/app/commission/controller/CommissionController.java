package com.neighbor.app.commission.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.neighbor.app.commission.entity.UserCommission;
import com.neighbor.app.commission.service.CommissionService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;

@Controller
@RequestMapping(value = "/commission")
@SessionAttributes("user")
public class CommissionController {
	private static final Logger logger = LoggerFactory.getLogger(CommissionController.class);
	@Autowired
	private CommissionService commissionService;
//	@Autowired
//	private UserService userService;
	
	@RequestMapping(value = "/queryCommission.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult queryCommissionPage(@ModelAttribute("user") UserInfo user,PageTools pageTools,UserCommission commission) {
		logger.info("queryCommissionPage request user >>> " +user.toString());
		logger.info("queryCommissionPage request pageTools >>> " +pageTools);
		logger.info("queryCommissionPage request commissionReq >>> " +commission);
		commission.setOwnUser(user.getId());
		commission.setPageTools(pageTools);
		
		Long size = commissionService.selectPageTotalCount(commission);
		pageTools.setTotal(size);
		List<UserCommission> resultList = commissionService.selectPageByObjectForList(commission);
		
		ResponseResult result = new ResponseResult();
		result.addBody("resultList", resultList);
		result.addBody("pageTools", pageTools);
		logger.info("queryCommissionPage response : " +pageTools);
		return result;
	}
	
	@RequestMapping(value = "/queryHistoryCommission.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult queryHistoryCommissionPage(@ModelAttribute("user") UserInfo user,PageTools pageTools,UserCommission commission) {
		logger.info("queryHistoryCommissionPage request user >>> " +user.toString());
		logger.info("queryHistoryCommissionPage request pageTools >>> " +pageTools);
		logger.info("queryHistoryCommissionPage request commissionReq >>> " +commission);
		commission.setOwnUser(user.getId());
		commission.setPageTools(pageTools);
		
		Long size = commissionService.selectPageTotalCount(commission);
		pageTools.setTotal(size);
		List<UserCommission> resultList = commissionService.selectPageByObjectForList(commission);
		
		ResponseResult result = new ResponseResult();
		result.addBody("resultList", resultList);
		result.addBody("pageTools", pageTools);
		logger.info("queryHistoryCommissionPage response : " +pageTools);
		return result;
	}
	
	@RequestMapping(value = "/queryRecommendAmount.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult queryRecommendAmount(@ModelAttribute("user") UserInfo user) {
		logger.info("queryRecommendAmount request user >>> " +user.toString());
		UserCommission commission = commissionService.selectAmountBy(user.getId());
		
		ResponseResult result = new ResponseResult();
		result.addBody("commission", commission);
		return result;
	}
}
