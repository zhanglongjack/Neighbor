package com.neighbor.app.chatlist.service.impl;

import com.neighbor.app.chatlist.dao.ChatListMapper;
import com.neighbor.app.chatlist.entity.ChatList;
import com.neighbor.app.chatlist.service.ChatListService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.DateFormateType;
import com.neighbor.common.util.DateUtils;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ChartListServiceImpl implements ChatListService {
    private static final Logger logger = LoggerFactory.getLogger(ChartListServiceImpl.class);
    @Autowired
    private ChatListMapper chatListMapper;

    @Override
    public ResponseResult chatlist(UserInfo user, ChatList chatList) throws Exception {
        logger.info("查询好友有聊天过的列表...");
        ResponseResult result = new ResponseResult();
        chatList.setUserId(user.getId());
        Long total = chatListMapper.selectPageTotalCount(chatList);
        List<ChatList> pageList = chatListMapper.selectPageByObjectForList(chatList);
        PageTools pageTools = chatList.getPageTools();
        pageTools.setTotal(total);
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
        ChatList chat = queryChatListInfo(chatList, userId, friendId);
        responseResult.addBody("chatInfo",chat);
        return responseResult;
    }

    private ChatList queryChatListInfo(ChatList chatList, Long userId, Long friendId) {
        ChatList queryChat = new ChatList();
        queryChat.setUserId(userId);
        queryChat.setFriendId(friendId);
        PageTools pageTools = new PageTools();
        chatList.setPageTools(pageTools);
        List<ChatList> chatLists = chatListMapper.selectPageByObjectForList(chatList);
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
        ChatList chat = queryChatListInfo(chatList, userId, friendId);
        responseResult.addBody("chatInfo",chat);
        return responseResult;
    }

    @Override
    public ResponseResult clearChatHistory(ChatList chatList) throws Exception {
        logger.info("清空好友聊天记录...");
        ResponseResult responseResult = new ResponseResult();
        //TODO 需要调用 好友消息服务清除
        return responseResult;
    }

    @Override
    public ResponseResult createChat(UserInfo user, ChatList chatList) throws Exception {
        ResponseResult responseResult = new ResponseResult();
        Long userId = user.getId();
        Long friendId = chatList.getFriendId();
        ChatList chat = queryChatListInfo(chatList, userId, friendId);
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
            ChatList friendChat = new ChatList();
            BeanUtils.copyProperties(createChat,friendChat);
            friendChat.setUserId(friendId);
            friendChat.setFriendId(userId);
            chatListMapper.insertSelective(friendChat);
        }
        return responseResult;
    }

    @Override
    public ResponseResult delChat(UserInfo user, ChatList chatList) throws Exception {
        ResponseResult responseResult = new ResponseResult();
        chatList.setUserId(user.getId());
        chatListMapper.delChat(chatList);
        ChatList delFriendChat = new ChatList();
        delFriendChat.setFriendId(user.getId());
        delFriendChat.setUserId(chatList.getFriendId());
        chatListMapper.delChat(delFriendChat);
        return responseResult;
    }


}
