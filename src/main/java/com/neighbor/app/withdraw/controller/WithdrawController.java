package com.neighbor.app.withdraw.controller;

import com.alibaba.fastjson.JSON;

import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.withdraw.entity.Withdraw;
import com.neighbor.app.withdraw.service.WithdrawService;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/withdraw")
@SessionAttributes("user")
public class WithdrawController {
    private static final Logger logger = LoggerFactory.getLogger(WithdrawController.class);
    @Autowired
    private WithdrawService withdrawService;

    @RequestMapping(value = "/withdraw.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult withdraw(@ModelAttribute("user") UserInfo user, Withdraw withdraw) throws Exception{
        logger.info("recharge request >>>> " + withdraw);
        logger.info("user info >>>> " + user);
        ResponseResult result = withdrawService.withdraw(user,withdraw);
        return result;
    }

    @RequestMapping(value = "/withdrawRecord.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult withdrawRecord(@ModelAttribute("user") UserInfo user, Withdraw withdraw) throws Exception{
        logger.info("withdrawRecord request user >>>> " + JSON.toJSONString(user));
        logger.info("withdrawRecord request withdraw >>>> " + JSON.toJSONString(withdraw));
        ResponseResult result  = withdrawService.withdrawRecord(user,withdraw);
        return result;
    }

    @RequestMapping(value = "/withdrawInfo.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult withdrawInfo(Withdraw withdraw) throws Exception{
        logger.info("withdrawInfo request >>>> " + JSON.toJSONString(withdraw));
        ResponseResult result = withdrawService.withdrawInfo(withdraw);
        return result;
    }
}

