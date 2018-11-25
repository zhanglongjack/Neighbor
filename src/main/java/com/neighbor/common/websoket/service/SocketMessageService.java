package com.neighbor.common.websoket.service;

import java.util.List;

import com.neighbor.common.websoket.po.SocketMessage;

public interface SocketMessageService {
    int deleteByPrimaryKey(Long msgId);

    int insertSelective(SocketMessage record);

    SocketMessage selectByPrimaryKey(Long msgId);

    int updateByPrimaryKeySelective(SocketMessage record);

	List<SocketMessage> selectByStatus(String status);

	List<SocketMessage> selectByTargetUserIdStatus(Long targetUserId, String status,String chatType);

	List<SocketMessage> selectbySelective(SocketMessage msg);

}