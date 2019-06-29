package com.neighbor.app.users.controller;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.neighbor.app.recharge.service.RechargeService;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.app.withdraw.service.WithdrawService;
import com.neighbor.common.constants.CommonConstants;
import com.neighbor.common.constants.EnvConstants;
import com.neighbor.common.util.BigDecimalUtil;

@Controller
public class IndexController {
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	@Autowired
	private WithdrawService withdrawService;
	@Autowired
	private RechargeService rechargeService;
	@Autowired
	private UserWalletService userWalletService;
	@Autowired
	private CommonConstants commonConstants;
	
	@RequestMapping(value="/index.req")
	public ModelAndView index(){
		logger.info("index request"); 
		ModelAndView mv = new ModelAndView("page/index"); 
		mv.addObject("test", "hello world !!!index");
		mv.addObject("date", new Date());
		
		return mv;
	}
	
	@RequestMapping(value="/index.ser")
	public ModelAndView indexServer(){
		logger.info("index request ser"); 
		String rateStr = commonConstants.getDictionarysBy(EnvConstants.PAYMENT, EnvConstants.PAYMENT_RATE);
		BigDecimal zero = new BigDecimal(0);
		
		BigDecimal sumRechargeAmount = rechargeService.querySumRechargeAmount();
		sumRechargeAmount = sumRechargeAmount==null?zero:sumRechargeAmount;
		BigDecimal withdrawAmountComplete = withdrawService.querySumWithdrawByComplete();
		withdrawAmountComplete = withdrawAmountComplete==null?zero:withdrawAmountComplete;
		BigDecimal withdrawAmountIncomplete = withdrawService.querySumWithdrawByIncomplete();
		withdrawAmountIncomplete = withdrawAmountIncomplete==null?zero:withdrawAmountIncomplete;
		BigDecimal sumLeftAmount = userWalletService.querySumLeftAmount();
		sumLeftAmount = sumLeftAmount==null?zero:sumLeftAmount;
		
		
//		净充值金额=所有玩家充值的总金额-所有玩家取现金额
//		总支出费用=(所有玩家充值的总金额*0.03) +(所有玩家取现金额*0.001)-(所有玩家取现金额*0.001)
//		当前净利润=净充值金额-当前所有玩家剩余金额-总支出费用
		BigDecimal netSumRechargeAmount = sumRechargeAmount.subtract(withdrawAmountComplete);
		BigDecimal sumExpense = sumRechargeAmount.multiply(new BigDecimal(rateStr));
		BigDecimal sumProfits = netSumRechargeAmount.subtract(sumLeftAmount.add(withdrawAmountIncomplete)).subtract(sumExpense);
		
		ModelAndView mv = new ModelAndView("page/index"); 
		mv.addObject("test", "hello world !!!index");
		mv.addObject("date", new Date());
		mv.addObject("sumProfits", BigDecimalUtil.rounding(sumProfits).toPlainString());
		
		return mv;
	}
	
	@RequestMapping(value="/home.req")
	public ModelAndView home(){
		logger.info("home request");
		ModelAndView mv = new ModelAndView("page/Home");
		mv.addObject("test", "hello world !!!home");
		mv.addObject("date", new Date());
//		mv.setViewName("forward:/login.html");
		return mv;
	}
}
