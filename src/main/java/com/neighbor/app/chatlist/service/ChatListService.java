package com.neighbor.app.chatlist.service;

import com.neighbor.app.chatlist.entity.ChatList;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.po.SocketMessage;

public interface ChatListService {
    public ResponseResult chatlist(UserInfo user, ChatList chatList) throws Exception;

    ResponseResult chatInfo(ChatList chatList) throws Exception;

    ResponseResult chatSetting(ChatList chatList) throws Exception;

    ResponseResult clearChatHistory(ChatList chatList)  throws Exception;

    ResponseResult createChat(UserInfo user, ChatList chatList) throws Exception;

    ResponseResult delChat(UserInfo user, ChatList chatList) throws Exception;

     ResponseResult jobDeleteChatHistory() throws Exception;

     void modifyLastMessage(SocketMessage socketMessage) throws Exception;
}
