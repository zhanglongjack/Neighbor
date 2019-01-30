package com.neighbor.app.recharge.controller;

import com.alibaba.fastjson.JSON;
import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.balance.entity.BalanceDetail;
import com.neighbor.app.recharge.constants.ChannelTypeDesc;
import com.neighbor.app.recharge.constants.RechargeStatusDesc;
import com.neighbor.app.recharge.entity.Recharge;
import com.neighbor.app.recharge.po.RechargeRecordResp;
import com.neighbor.app.recharge.service.RechargeService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/recharge")
@SessionAttributes("user")
public class RechargeController {
	private static final Logger logger = LoggerFactory.getLogger(RechargeController.class);
	@Autowired
	private RechargeService rechargeService;
	//
	// @RequestMapping(value = "/rechargeRecord.req",method= RequestMethod.POST)
	// @ResponseBody
	// public ResponseResult rechargeRecord(@ModelAttribute("user") UserInfo
	// user,Recharge req) throws Exception{
	// logger.info("user info >>>> " + JSON.toJSONString(user));
	// logger.info("rechargeRecord request : " + JSON.toJSONString(req));
	// RechargeRecordResp rechargeRecordResp =
	// rechargeService.rechargeRecord(user,req);
	// ResponseResult result = new ResponseResult();
	// result.addBody("resp", rechargeRecordResp);
	// return result;
	// }

	//检查是否是超管和客服的角色账号
	private boolean checkUserRole(UserInfo userInfo){
		if("1".equals(userInfo.getUserRole())||"2".equals(userInfo.getUserRole())){
			return true;
		}
		return false;
	}
	@RequestMapping(value = "/rechargeRecord.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult rechargeRecord(@ModelAttribute("user") UserInfo user, PageTools pageTools,Recharge recharge) throws Exception {
		// 实体类重写toString后不用json转换也可以,json转换是有消耗性能的
		logger.info("user info >>>> " + user);
		logger.info("rechargeRecord request : " + pageTools);
		logger.info("recharge request : " + recharge);
		ResponseResult result = new ResponseResult();
		Recharge detail = new Recharge();
		if(recharge!=null&&"1".equals(recharge.getIsCustomer())){
			//后面增加特定用户判断
			if(!checkUserRole(user)){
				result.setErrorCode(ErrorCodeDesc.failed.getValue());
				result.setErrorMessage("很抱歉，你没有权限操作~");
				return result;
			}
			detail.setStates(RechargeStatusDesc.initial.toString());
			detail.setChannelType(ChannelTypeDesc.offline.toString());
		}else{
			detail.setuId(user.getId());
		}
		detail.setPageTools(pageTools);

		Long size = rechargeService.selectPageTotalCount(detail);
		pageTools.setTotal(size);
		List<Recharge> resultList = rechargeService.selectPageByObjectForList(detail);

		result.addBody("resultList", resultList);
		result.addBody("pageTools", pageTools);
		logger.info("rechargeRecord response : " + pageTools);
		return result;
	}

	@RequestMapping(value = "/recharge.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult recharge(@ModelAttribute("user") UserInfo user, Recharge recharge) throws Exception {
		logger.info("recharge request >>>> " + recharge);
		logger.info("user info >>>> " + user);
		ResponseResult result = rechargeService.recharge(user, recharge);
		return result;
	}

	@RequestMapping(value = "/modifyRecharge.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult modifyRecharge(@ModelAttribute("user") UserInfo user, Recharge recharge) throws Exception {
		logger.info("recharge request >>>> " + recharge);
		logger.info("user info >>>> " + user);
		//后面增加特定用户判断
		ResponseResult result = new ResponseResult();
		if(!checkUserRole(user)){
			result.setErrorCode(ErrorCodeDesc.failed.getValue());
			result.setErrorMessage("很抱歉，你没有权限操作~");
			return result;
		}
		result = rechargeService.modifyRecharge(user, recharge);
		return result;
	}

	@RequestMapping(value = "/rechargeInfo.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult rechargeInfo(Recharge recharge) throws Exception {
		logger.info("recharge request >>>> " + recharge);
		ResponseResult result = rechargeService.rechargeInfo(recharge);
		return result;
	}
}
