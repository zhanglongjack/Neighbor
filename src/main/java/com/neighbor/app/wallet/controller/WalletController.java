package com.neighbor.app.wallet.controller;

import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.packet.service.PacketService;
import com.neighbor.common.exception.ParamsCheckException;
import com.neighbor.common.security.EncodeData;
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
import org.thymeleaf.util.StringUtils;

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
		wallet.setPayPassword(wallet.getPayPassword()==null?null:"******");
		ResponseResult result = new ResponseResult();
		result.addBody("wallet", wallet);
		return result;
	}

	@RequestMapping(value="/payPasswordEdit.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult payPasswordEdit(@ModelAttribute("user")UserInfo user,UserWallet userWallet) throws Exception{
		logger.info("payPasswordEdit request user :{} ",user);
		logger.info("payPasswordEdit request userWallet :{} ",userWallet);
		if(userWallet==null||StringUtils.isEmpty(userWallet.getPayPassword())){
			logger.info("设置支付密码不能为空！");
			throw new ParamsCheckException(ErrorCodeDesc.failed.getValue(),"设置支付密码不能为空!");
		}
		UserWallet update = new UserWallet();
		update.setuId(user.getId());
		update.setPayPassword(EncodeData.encode(userWallet.getPayPassword()));
		userWalletService.payPasswordEdit(update);
		return new ResponseResult();
	}

	@RequestMapping(value="/payPasswordValid.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult payPasswordValid(@ModelAttribute("user")UserInfo user,UserWallet userWallet) throws Exception{
		logger.info("payPasswordEdit request user :{} ",user);
		logger.info("payPasswordEdit request userWallet :{} ",userWallet);
		UserWallet wallet = userWalletService.selectByPrimaryUserId(user.getId());
		if(wallet==null|| StringUtils.isEmpty(wallet.getPayPassword()) ||userWallet==null
				|| StringUtils.isEmpty(userWallet.getPayPassword())||!EncodeData.matches(userWallet.getPayPassword(), wallet.getPayPassword())){
			logger.info("支付密码错误！");
			throw new ParamsCheckException(ErrorCodeDesc.failed.getValue(),"支付密码错误!");
		}
		return new ResponseResult();
	}
	
}
