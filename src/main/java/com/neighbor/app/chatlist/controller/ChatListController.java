package com.neighbor.app.chatlist.controller;

import com.neighbor.app.chatlist.entity.ChatList;
import com.neighbor.app.chatlist.service.ChatListService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/chatlist")
@SessionAttributes("user")
public class ChatListController {
    private static final Logger logger = LoggerFactory.getLogger(ChatListController.class);
    @Autowired
    private ChatListService chatListService;


    @RequestMapping(value = "/createChat.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult createChat(@ModelAttribute("user") UserInfo user,ChatList chatList) throws Exception{
        logger.info("chatlist request user >>>> " + user);
        logger.info("chatlist request chatList >>>> " + chatList);
        ResponseResult result  = chatListService.createChat(user,chatList);
        return result;
    }

    @RequestMapping(value = "/delChat.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult delChat(@ModelAttribute("user") UserInfo user,ChatList chatList) throws Exception{
        logger.info("chatlist request user >>>> " + user);
        logger.info("chatlist request chatList >>>> " + chatList);
        ResponseResult result  = chatListService.delChat(user,chatList);
        return result;
    }

    @RequestMapping(value = "/listRecord.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult listRecord(@ModelAttribute("user") UserInfo user, PageTools pageTools) throws Exception{
        logger.info("chatlist request user >>>> " + user);
        ChatList chatList = new ChatList();
        chatList.setPageTools(pageTools);
        logger.info("chatlist request chatList >>>> " + chatList);
        ResponseResult result  = chatListService.chatlist(user,chatList);
        return result;
    }

    @RequestMapping(value = "/chatInfo.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult chatInfo(@ModelAttribute("user") UserInfo user,ChatList chatList) throws Exception {
        logger.info("chatInfo request user >>>> " + user);
        chatList.setUserId(user.getId());
        logger.info("chatInfo request >>>> " + chatList);
        ResponseResult result = chatListService.chatInfo(chatList);
        return result;
    }

    @RequestMapping(value = "/chatSetting.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult chatSetting(@ModelAttribute("user") UserInfo user,ChatList chatList) throws Exception {
        logger.info("chatInfo request user >>>> " + user);
        chatList.setUserId(user.getId());
        logger.info("chatInfo request >>>> " + chatList);
        ResponseResult result = chatListService.chatSetting(chatList);
        return result;
    }

    @RequestMapping(value = "/clearChatHistory.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult clearChatHistory(@ModelAttribute("user") UserInfo user,ChatList chatList) throws Exception {
        logger.info("chatInfo request user >>>> " + user);
        chatList.setUserId(user.getId());
        logger.info("chatInfo request >>>> " + chatList);
        ResponseResult result = chatListService.clearChatHistory(chatList);
        return result;
    }

}
