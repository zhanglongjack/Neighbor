package com.neighbor.app.friend.controller;

import com.neighbor.app.bankcard.controller.BankCardController;
import com.neighbor.app.bankcard.entity.BankCard;
import com.neighbor.app.friend.entity.Friend;
import com.neighbor.app.friend.service.FriendService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/friend")
@SessionAttributes("user")
public class FriendController {
    private static final Logger logger = LoggerFactory.getLogger(FriendController.class);

    @Autowired
    private FriendService friendService;

    @RequestMapping(value = "/listRecord.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult listRecord(@ModelAttribute("user") UserInfo user, PageTools pageTools) throws Exception{
        logger.info("withdrawRecord request user >>>> " + user);
        Friend friend = new Friend();
        friend.setPageTools(pageTools);
        logger.info("withdrawRecord request friend >>>> " + friend);
        ResponseResult result  = friendService.listRecord(user,friend);
        return result;
    }
}
