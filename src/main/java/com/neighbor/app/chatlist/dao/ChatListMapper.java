package com.neighbor.app.chatlist.dao;

import com.neighbor.app.chatlist.entity.ChatList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatListMapper {

    int deleteByPrimaryKey(Long id);

    int insertSelective(ChatList record);

    ChatList selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ChatList record);

    Long selectPageTotalCount(ChatList record);

    List<ChatList> selectPageByObjectForList(ChatList record);

    List<ChatList> selectAll();

    int delChat(ChatList chatList);
}