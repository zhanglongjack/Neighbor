package com.neighbor.app.withdraw.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.withdraw.constants.WithdrawStatusDesc;
import com.neighbor.app.withdraw.entity.Withdraw;
import com.neighbor.app.withdraw.service.WithdrawService;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.po.SocketMessage;
import com.neighbor.common.websoket.util.WebSocketPushHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(value = "/withdrawList")
@SessionAttributes("user")
public class WithdrawListController {
    private static final Logger logger = LoggerFactory.getLogger(WithdrawListController.class);
    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private WebSocketPushHandler webSocketPushHandler;

    @RequestMapping(value = "/primaryModalView.ser")
    public String primaryModalView(Withdraw withdraw, String modifyModel, Model model) throws Exception {
        logger.debug("primaryModalView request:" + withdraw + ",model:" + model);
        model.addAttribute("modifyModel", modifyModel);
        model.addAttribute("withdraw", withdraw);
        logger.debug("primaryModalView model : " + model);

        return "page/withdraw/ModifyModal";
    }

    @RequestMapping(value = "/pageView.ser")
    @ResponseBody
    public Map<String, Object> pageView(UserInfo userInfo,Withdraw withdraw, PageTools pageTools                                         ) {
        logger.debug("withdrawList request userInfo: " +userInfo);
        if(withdraw==null||withdraw.getStates()==null){
            withdraw = new Withdraw();
            withdraw.setStates(WithdrawStatusDesc.initial.toString());
        }
        Long size = withdrawService.selectPageTotalCount(withdraw);
        pageTools.setTotal(size);
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("pageTools", pageTools);
        return result;
    }

    @RequestMapping(value = "/loadPage.ser")
    public ModelAndView loadPage(UserInfo userInfo, PageTools pageTools,
                                 Withdraw withdraw) throws Exception {
        logger.debug("loadPage User request:" + userInfo + " page info ===" + pageTools+" withdraw ==="+withdraw);
        if(withdraw==null||withdraw.getStates()==null){
            withdraw = new Withdraw();
            withdraw.setStates(WithdrawStatusDesc.initial.toString());
        }

        userInfo.setPageTools(pageTools);
        ModelAndView mv = new ModelAndView("page/withdraw/Content :: container-fluid");
        Long size = withdrawService.selectPageTotalCount(withdraw);
        pageTools.setTotal(size);
        List<Withdraw> ciList = withdrawService.selectPageByObjectForList(withdraw);
        logger.debug("loadPage User result list info =====:" + ciList);

        mv.addObject("resultList", ciList);
        mv.addObject("pageTools", pageTools);
        mv.addObject("queryObject", withdraw);

        return mv;
    }

    @RequestMapping(value="/auditEdit.ser")
    @ResponseBody
    public Map<String,Object> auditEdit(@ModelAttribute("user") UserInfo user,Withdraw withdraw){
        logger.info("auditEdit userInfo:{}",user);
        logger.info("auditEdit withdraw:{}",withdraw);
        Map<String,Object> map = new HashMap<String,Object>();
        int num = 0;
        try {
            ResponseResult result = withdrawService.modifyWithdraw(user,withdraw);
            logger.info("result >>>>>"+result);
            if(ErrorCodeDesc.success.getValue()==result.getErrorCode()){
                if(WithdrawStatusDesc.failed.toString().equals(withdraw.getStates())){
                    String nickName = StringUtils.isEmpty(user.getNickName())?user.getId()+"":user.getNickName();
                    webSocketPushHandler.walletRefreshNotice(user.getId(),withdraw.getuId(),nickName);
                }
                map.put("success", true);
                num=1;
            }else{
                map.put("success", false);
            }
        } catch (Exception e) {
            map.put("success", false);
            logger.error(e.getMessage(),e);
        }
        map.put("editNumber", num);
        return map;
    }
}

