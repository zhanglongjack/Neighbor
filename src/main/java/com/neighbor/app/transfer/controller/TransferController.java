package com.neighbor.app.transfer.controller;

import com.alibaba.fastjson.JSON;
import com.neighbor.app.transfer.entity.Transfer;
import com.neighbor.app.transfer.po.TransferReq;
import com.neighbor.app.transfer.service.TransferService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/transfer")
@SessionAttributes("user")
public class TransferController {
    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);
    @Autowired
    private TransferService transferService;

    @RequestMapping(value = "/transfer.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult transfer(@ModelAttribute("user") UserInfo user, TransferReq transfer) throws Exception{
        logger.info("transfer request : " + transfer);
        ResponseResult result  = transferService.transfer(user,transfer);
        return result;
    }

    @RequestMapping(value = "/transferRecord.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult transferRecord(@ModelAttribute("user") UserInfo user, PageTools pageTools) throws Exception{
        logger.info("transferRecord request user >>>> " + user);
        Transfer transfer = new Transfer();
        transfer.setPageTools(pageTools);
        logger.info("transferRecord request transfer >>>> " + transfer);
        ResponseResult result  = transferService.transferRecord(user,transfer);
        return result;
    }

    @RequestMapping(value = "/transferInfo.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult transferInfo(Transfer transfer) throws Exception{
        logger.info("transferInfo request >>>> " + JSON.toJSONString(transfer));
        ResponseResult result = transferService.transferInfo(transfer);
        return result;
    }
}
