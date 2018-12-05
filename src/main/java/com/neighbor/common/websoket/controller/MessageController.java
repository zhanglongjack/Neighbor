package com.neighbor.common.websoket.controller;

import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.constants.MessageStatus;
import com.neighbor.common.websoket.service.SocketMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/message")
@SessionAttributes("user")
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    private SocketMessageService socketMessageService;

    //查询出所有未读消息
    @RequestMapping(value = "/unreadRecord.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult unreadRecord(@ModelAttribute("user") UserInfo user) throws Exception{
        logger.info("unreadRecord request user >>>> " + user);
        ResponseResult result  = socketMessageService.unreadRecord(user);
        return result;
    }

    //分页展示好友发送已完成和自己发送的消息
    @RequestMapping(value = "/pageRecord.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult pageRecord(@ModelAttribute("user") UserInfo user,Long friendId,Long msgId,PageTools pageTools) throws Exception{
        logger.info("pageRecord request user >>>> " + user+" | targetUserId >>"+friendId+" | msgId >> "+msgId);
        logger.info("pageRecord request pageTools >>>> " + pageTools);
        ResponseResult result  = socketMessageService.pageRecord(user,friendId,msgId,pageTools);
        return result;
    }

    //通知后端变更完成记录状态
    @RequestMapping(value = "/changeCompleteRecord.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult changeCompleteRecord(@ModelAttribute("user") UserInfo user,Long friendId,Long msgId) throws Exception{
        logger.info("pageRecord request user >>>> " + user+" | targetUserId >>"+friendId +" | msgId >> "+msgId+"| status >>"+ MessageStatus.complete);
        ResponseResult result  = socketMessageService.changeRecord(user,friendId,msgId,MessageStatus.complete.toString());
        return result;
    }

    //通知后端变更已推送记录状态
    @RequestMapping(value = "/changePushedRecord.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult changePushedRecord(@ModelAttribute("user") UserInfo user,Long friendId,Long msgId) throws Exception{
        logger.info("pageRecord request user >>>> " + user+" | targetUserId >>"+friendId +" | msgId >> "+msgId+"| status >>"+MessageStatus.pushed.toString());
        ResponseResult result  = socketMessageService.changeRecord(user,friendId,msgId,MessageStatus.pushed.toString());
        return result;
    }

}
