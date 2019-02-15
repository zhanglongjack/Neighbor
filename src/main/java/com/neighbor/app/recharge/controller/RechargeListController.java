package com.neighbor.app.recharge.controller;

import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.recharge.constants.RechargeStatusDesc;
import com.neighbor.app.recharge.entity.Recharge;
import com.neighbor.app.recharge.service.RechargeService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.withdraw.constants.WithdrawStatusDesc;
import com.neighbor.app.withdraw.entity.Withdraw;
import com.neighbor.app.withdraw.service.WithdrawService;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/rechargeList")
@SessionAttributes("user")
public class RechargeListController {
    private static final Logger logger = LoggerFactory.getLogger(RechargeListController.class);
    @Autowired
    private RechargeService rechargeService;

    @RequestMapping(value = "/primaryModalView.ser")
    public String primaryModalView(Recharge recharge, String modifyModel, Model model) throws Exception {
        logger.debug("primaryModalView request:" + recharge + ",model:" + model);
        model.addAttribute("modifyModel", modifyModel);
        model.addAttribute("recharge", recharge);
        logger.debug("primaryModalView model : " + model);

        return "page/recharge/ModifyModal";
    }
    @RequestMapping(value = "/imgView.ser")
    public String imgView(String imgSrc, String modifyModel, Model model) throws Exception {
        logger.debug("primaryModalView request:" + imgSrc + ",model:" + model);
        model.addAttribute("modifyModel", modifyModel);
        model.addAttribute("imgSrc", imgSrc);
        logger.debug("primaryModalView model : " + model);

        return "page/recharge/ImageModal";
    }


    @RequestMapping(value = "/pageView.ser")
    @ResponseBody
    public Map<String, Object> pageView(UserInfo userInfo, Recharge recharge, PageTools pageTools                                         ) {
        logger.debug("withdrawList request userInfo: " +userInfo);
        if(recharge==null||recharge.getStates()==null){
            recharge = new Recharge();
            recharge.setStates(RechargeStatusDesc.initial.toString());
        }
        Long size = rechargeService.selectPageTotalCount(recharge);
        pageTools.setTotal(size);
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("pageTools", pageTools);
        return result;
    }

    @RequestMapping(value = "/loadPage.ser")
    public ModelAndView loadPage(UserInfo userInfo, PageTools pageTools,
                                 Recharge recharge) throws Exception {
        logger.debug("loadPage User request:" + userInfo + " page info ===" + pageTools+" recharge ==="+recharge);
        if(recharge==null||recharge.getStates()==null){
            recharge = new Recharge();
            recharge.setStates(RechargeStatusDesc.initial.toString());
        }

        userInfo.setPageTools(pageTools);
        ModelAndView mv = new ModelAndView("page/recharge/Content :: container-fluid");
        Long size = rechargeService.selectPageTotalCount(recharge);
        pageTools.setTotal(size);
        List<Recharge> ciList = rechargeService.selectPageByObjectForList(recharge);
        logger.debug("loadPage User result list info =====:" + ciList);

        mv.addObject("resultList", ciList);
        mv.addObject("pageTools", pageTools);
        mv.addObject("queryObject", recharge);

        return mv;
    }

    @RequestMapping(value="/auditEdit.ser")
    @ResponseBody
    public Map<String,Object> auditEdit(@ModelAttribute("user") UserInfo user,Recharge recharge){
        logger.info("auditEdit userInfo:{}",user);
        logger.info("auditEdit recharge:{}",recharge);
        Map<String,Object> map = new HashMap<String,Object>();
        int num = 0;
        try {
            ResponseResult result = rechargeService.modifyRecharge(user,recharge);
            logger.info("result >>>>>"+result);
            if(ErrorCodeDesc.success.getValue()==result.getErrorCode()){
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

