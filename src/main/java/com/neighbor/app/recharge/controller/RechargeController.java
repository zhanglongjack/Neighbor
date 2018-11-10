package com.neighbor.app.recharge.controller;

import com.alibaba.fastjson.JSON;
import com.neighbor.app.recharge.entity.Recharge;
import com.neighbor.app.recharge.po.RechargeRecordResp;
import com.neighbor.app.recharge.service.RechargeService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.ResponseResult;
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

    @RequestMapping(value = "/rechargeRecord.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult rechargeRecord(@ModelAttribute("user") UserInfo user,Recharge req) throws Exception{
        logger.info("user info >>>> " + JSON.toJSONString(user));
        logger.info("rechargeRecord request : " + JSON.toJSONString(req));
        RechargeRecordResp rechargeRecordResp = rechargeService.rechargeRecord(user,req);
        ResponseResult result = new ResponseResult();
        result.addBody("resp", rechargeRecordResp);
        return result;
    }

    @RequestMapping(value = "/recharge.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult recharge(@ModelAttribute("user") UserInfo user,Recharge recharge) throws Exception{
        logger.info("recharge request >>>> " + JSON.toJSONString(recharge));
        logger.info("user info >>>> " + JSON.toJSONString(recharge));
        ResponseResult result = rechargeService.recharge(user,recharge);
        return result;
    }

    @RequestMapping(value = "/rechargeInfo.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult rechargeInfo(Recharge recharge) throws Exception{
        logger.info("recharge request >>>> " + JSON.toJSONString(recharge));
        ResponseResult result = rechargeService.rechargeInfo(recharge);
        return result;
    }
}
