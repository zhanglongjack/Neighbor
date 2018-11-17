package com.neighbor.app.bankcard.controller;
import com.neighbor.app.bankcard.entity.BankCard;
import com.neighbor.app.bankcard.service.BankCardService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/bankcard")
@SessionAttributes("user")
public class BankCardController {
    private static final Logger logger = LoggerFactory.getLogger(BankCardController.class);
    @Autowired
    private BankCardService bankCardService;


    @RequestMapping(value = "/addCard.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult addCard(@ModelAttribute("user") UserInfo user, BankCard bankCard) throws Exception{
        logger.info("addCard request bankCard >>>> " + bankCard);
        logger.info("user info >>>> " + user);
        ResponseResult result = bankCardService.addCard(user,bankCard);
        return result;
    }

    @RequestMapping(value = "/listRecord.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult listRecord(@ModelAttribute("user") UserInfo user, PageTools pageTools) throws Exception{
        logger.info("withdrawRecord request user >>>> " + user);
        BankCard bankCard = new BankCard();
        bankCard.setPageTools(pageTools);
        logger.info("withdrawRecord request bankCard >>>> " + bankCard);
        ResponseResult result  = bankCardService.listRecord(user,bankCard);
        return result;
    }

}
