package com.neighbor.common.websoket.service;

import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.po.SocketMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SocketMessageService {
    int deleteByPrimaryKey(Long msgId);

    int insertSelective(SocketMessage record);

    SocketMessage selectByPrimaryKey(Long msgId);

    int updateByPrimaryKeySelective(SocketMessage record);

	List<SocketMessage> selectByStatus(String status);

	List<SocketMessage> selectByTargetUserIdStatus(Long targetUserId, String status,String chatType);

	List<SocketMessage> selectbySelective(SocketMessage msg);

	void insertRelationShipSelective(Map<String, Long> relationShip);

    ResponseResult unreadRecord(UserInfo user);

    ResponseResult pageRecord(UserInfo user, Long targetUserId,Long msgId, PageTools pageTools);

    ResponseResult changeRecord(UserInfo user, Long targetUserId, Long msgId, String status);

    Long selectPageTotalCount(HashMap map);

    List<SocketMessage> selectPageByObjectForList(HashMap map);

	List<SocketMessage> selectForTargetUserMsgByStatus(String status);
	int deleteMessage(SocketMessage record);
    int jobDeleteMessage(SocketMessage record);
}