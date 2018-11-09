package com.neighbor.app.wallet.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.common.util.ResponseResult;

@Controller
@RequestMapping(value = "/wallet")
@SessionAttributes("user")
public class WalletController {
	private static final Logger logger = LoggerFactory.getLogger(WalletController.class);
	@Autowired
	private UserWalletService userWalletService; 

	@RequestMapping(value = "/walletShow.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult userInfoShow(@ModelAttribute("user") UserInfo user) {
		logger.info("userView request : " +user);
		UserWallet wallet = userWalletService.selectByPrimaryUserId(user.getId());
		
		ResponseResult result = new ResponseResult();
		result.addBody("wallet", wallet);
		return result;
	}
	
}
