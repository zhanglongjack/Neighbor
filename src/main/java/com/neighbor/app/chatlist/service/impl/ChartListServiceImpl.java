package com.neighbor.app.chatlist.service.impl;

import com.neighbor.app.chatlist.constants.ChatHistorySetDesc;
import com.neighbor.app.chatlist.dao.ChatListMapper;
import com.neighbor.app.chatlist.entity.ChatList;
import com.neighbor.app.chatlist.service.ChatListService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.*;
import com.neighbor.common.websoket.constants.MessageDeleteStates;
import com.neighbor.common.websoket.po.SocketMessage;
import com.neighbor.common.websoket.service.SocketMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ChartListServiceImpl implements ChatListService {
    private static final Logger logger = LoggerFactory.getLogger(ChartListServiceImpl.class);
    @Autowired
    private ChatListMapper chatListMapper;
    @Autowired
    private SocketMessageService socketMessageService;

    @Override
    public ResponseResult chatlist(UserInfo user, ChatList chatList) throws Exception {
        logger.info("查询好友有聊天过的列表...");
        ResponseResult result = new ResponseResult();
        chatList.setUserId(user.getId());
        Long total = chatListMapper.selectPageTotalCount(chatList);
        List<ChatList> pageList = chatListMapper.selectPageByObjectForList(chatList);
        PageTools pageTools = chatList.getPageTools();
        pageTools.setTotal(total);
        if(pageList!=null&&pageList.size()>0){
            for(ChatList cl : pageList){
                HashMap hashMap = new HashMap(2);
                hashMap.put("userId",cl.getUserId());
                hashMap.put("friendId",cl.getFriendId());
                hashMap.put("rowIndex",0);
                hashMap.put("pageSize",1);
                List<SocketMessage> socketMessageList = socketMessageService.selectPageByObjectForList(hashMap);
                if(socketMessageList!=null&&socketMessageList.size()>0){
                    SocketMessage socketMessage = socketMessageList.get(0);
                    cl.setLastChatMessageId(socketMessage.getMsgId());
                    cl.setLastChatMessageType(socketMessage.getMsgType());
                    cl.setLastChatMessageContent(socketMessage.getContent());
                    cl.setLastChatDateTime(DateUtils.formatDate(socketMessage.getDate()+" "+socketMessage.getTime(),DateFormateType.LANG_FORMAT));
                    cl.setLastChatDate(socketMessage.getDate());
                    cl.setLastChatTime(socketMessage.getTime());
                }else{
                    cl.setLastChatMessageId(null);
                    cl.setLastChatMessageType(null);
                    cl.setLastChatMessageContent(null);
                    cl.setLastChatDateTime(null);
                    cl.setLastChatDate(null);
                    cl.setLastChatTime(null);
                }
            }
        }


        result.addBody("resultList", pageList);
        result.addBody("pageTools", pageTools);
        return result;
    }

    @Override
    public ResponseResult chatInfo(ChatList chatList) throws Exception {
        logger.info("查询聊天界面查看好友信息...");
        ResponseResult responseResult = new ResponseResult();
        Long userId = chatList.getUserId();
        Long friendId = chatList.getFriendId();
        ChatList chat = queryChatListInfo(userId, friendId);
        responseResult.addBody("chatInfo",chat);
        return responseResult;
    }

    private ChatList queryChatListInfo( Long userId, Long friendId) {
        ChatList queryChat = new ChatList();
        queryChat.setUserId(userId);
        queryChat.setFriendId(friendId);
        PageTools pageTools = new PageTools();
        queryChat.setPageTools(pageTools);
        List<ChatList> chatLists = chatListMapper.selectPageByObjectForList(queryChat);
        ChatList chat = null;
        if(chatLists!=null&&chatLists.size()>0){
            chat = chatLists.get(0);
        }
        return chat;
    }

    @Override
    public ResponseResult chatSetting(ChatList chatList) throws Exception {
        logger.info("查询聊天好友设置界面...");
        ResponseResult responseResult = new ResponseResult();
        chatListMapper.updateByFriendKeySelective(chatList);
        Long userId = chatList.getUserId();
        Long friendId = chatList.getFriendId();
        ChatList chat = queryChatListInfo( userId, friendId);
        responseResult.addBody("chatInfo",chat);
        return responseResult;
    }

    @Override
    public ResponseResult clearChatHistory(ChatList chatList) throws Exception {
        logger.info("清空好友聊天记录...");
        ResponseResult responseResult = new ResponseResult();
        //删除 所有我发送给好友的记录
        SocketMessage selfSend = new SocketMessage();
        selfSend.setSendUserId(chatList.getUserId());
        selfSend.setTargetUserId(chatList.getFriendId());
        selfSend.setSendUserDeleteFlag(MessageDeleteStates.delete.getDes());
        socketMessageService.deleteMessage(selfSend);
        //删除 所有发送给我的记录
        SocketMessage friendSend = new SocketMessage();
        friendSend.setSendUserId(chatList.getFriendId());
        friendSend.setTargetUserId(chatList.getUserId());
        friendSend.setTargetUserDeleteFlag(MessageDeleteStates.delete.getDes());
        socketMessageService.deleteMessage(friendSend);
        return responseResult;
    }

    public ResponseResult jobDeleteChatHistory() throws Exception {
        logger.info("JOB清空好友聊天记录...");
        ResponseResult responseResult = new ResponseResult();
        ChatList select = new ChatList();
        select.setChatHistorySetNot(ChatHistorySetDesc.save.toString());
        List<ChatList> chatLists =  chatListMapper.selectAll(select);
        for(ChatList chatList :chatLists){
            deleteChatHistory(chatList);
        }
        return responseResult;
    }

    @Override
    public void modifyLastMessage(SocketMessage socketMessage) throws Exception {
        //单条消息才更新。
        if((socketMessage.getTargetGroupId()==null||socketMessage.getTargetGroupId()==0)&&"1".equals(socketMessage.getMasterMsgType())){
            ChatList left = new ChatList();
            Date chatLastDate = DateUtils.formatDate(socketMessage.getDate()+" "+socketMessage.getTime(),DateFormateType.LANG_FORMAT);
            left.setLastChatMessageContent(socketMessage.getContent());
            left.setLastChatDate(socketMessage.getDate());
            left.setLastChatTime(socketMessage.getTime());
            left.setUserId(socketMessage.getSendUserId());
            left.setFriendId(socketMessage.getTargetUserId());
            left.setLastChatMessageType(socketMessage.getMsgType());
            left.setLastChatMessageId(socketMessage.getMsgId());
            left.setLastChatDateTime(chatLastDate);
            chatListMapper.updateByFriendKeySelective(left);
            ChatList right = new ChatList();
            right.setLastChatMessageContent(socketMessage.getContent());
            right.setLastChatDate(socketMessage.getDate());
            right.setLastChatTime(socketMessage.getTime());
            right.setUserId(socketMessage.getTargetUserId());
            right.setFriendId(socketMessage.getSendUserId());
            right.setLastChatMessageType(socketMessage.getMsgType());
            right.setLastChatMessageId(socketMessage.getMsgId());
            right.setLastChatDateTime(chatLastDate);
            chatListMapper.updateByFriendKeySelective(right);
        }
    }

    @Transactional(readOnly = false,rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public ResponseResult deleteChatHistory(ChatList chatList) throws Exception {
        logger.info("JOB 执行到 chatList id "+chatList.getId()+" 间隔 "+chatList.getChatHistorySetText()+ " 清空好友聊天记录...");
        ResponseResult responseResult = new ResponseResult();
        //删除 所有我发送给好友的记录
        SocketMessage selfSend = new SocketMessage();
        selfSend.setSendUserId(chatList.getUserId());
        selfSend.setTargetUserId(chatList.getFriendId());
        selfSend.setSendUserDeleteFlag(MessageDeleteStates.delete.getDes());
        selfSend.setCleanHistory(chatList.getChatHistorySet());
        socketMessageService.jobDeleteMessage(selfSend);
        //删除 所有发送给我的记录
        SocketMessage friendSend = new SocketMessage();
        friendSend.setSendUserId(chatList.getFriendId());
        friendSend.setTargetUserId(chatList.getUserId());
        friendSend.setTargetUserDeleteFlag(MessageDeleteStates.delete.getDes());
        friendSend.setCleanHistory(chatList.getChatHistorySet());
        socketMessageService.jobDeleteMessage(friendSend);
        return responseResult;
    }

    @Override
    public ResponseResult createChat(UserInfo user, ChatList chatList) throws Exception {
        ResponseResult responseResult = new ResponseResult();
        Long userId = user.getId();
        Long friendId = chatList.getFriendId();
        ChatList chat = queryChatListInfo(userId, friendId);
        if(chat==null){
            ChatList createChat = new ChatList();
            createChat.setUserId(userId);
            createChat.setFriendId(friendId);
            java.util.Date date = new Date();
            createChat.setCreateTime(date);
            String[] dateStr = DateUtils.formatDateStr(date, DateFormateType.LANG_FORMAT).split(" ");
            createChat.setCreDate(dateStr[0]);
            createChat.setCreTime(dateStr[1]);
            createChat.setLastChatDate(createChat.getCreDate());
            createChat.setLastChatDateTime(date);
            createChat.setLastChatTime(createChat.getCreTime());
            chatListMapper.insertSelective(createChat);
            ChatList chatNew = queryChatListInfo(userId, friendId);
            responseResult.addBody("chatList",chatNew);
        }else{
            responseResult.addBody("chatList",chat);
        }
        return responseResult;
    }

    @Override
    public ResponseResult delChat(UserInfo user, ChatList chatList) throws Exception {
        ResponseResult responseResult = new ResponseResult();
        chatList.setUserId(user.getId());
        chatListMapper.delChat(chatList);
        //删除聊天窗口清空聊天记录
        ChatList chat = queryChatListInfo( user.getId(), chatList.getFriendId());
        if(chat!=null){
            clearChatHistory(chat);
        }
        return responseResult;
    }


}
