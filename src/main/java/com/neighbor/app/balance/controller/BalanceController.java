package com.neighbor.app.balance.controller;

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

import com.neighbor.app.balance.entity.BalanceDetail;
import com.neighbor.app.balance.po.TransactionSubTypeDesc;
import com.neighbor.app.balance.po.TransactionTypeDesc;
import com.neighbor.app.balance.service.BalanceDetailService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;

@Controller
@RequestMapping(value = "/balance")
@SessionAttributes("user")
public class BalanceController {
	private static final Logger logger = LoggerFactory.getLogger(BalanceController.class);
	@Autowired
	private BalanceDetailService balanceDetailService; 

	@RequestMapping(value = "/balancePage.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult balancePage(@ModelAttribute("user") UserInfo user,PageTools pageTools) {
		logger.info("balancePage request : " +pageTools);
		BalanceDetail detail = new BalanceDetail();
		detail.setuId(user.getId());
		detail.setPageTools(pageTools);
		
		Long size = balanceDetailService.selectPageTotalCount(detail);
		pageTools.setTotal(size);
		List<BalanceDetail> resultList = balanceDetailService.selectPageByObjectForList(detail);
		
		ResponseResult result = new ResponseResult();
		result.addBody("resultList", resultList);
		result.addBody("pageTools", pageTools);
		logger.info("balancePage response : " +pageTools);
		return result;
	}

	
}
