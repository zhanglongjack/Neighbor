package com.neighbor.common.websoket.service;

import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.constants.MessageStatus;
import com.neighbor.common.websoket.constants.WebSocketChatType;
import com.neighbor.common.websoket.po.GroupMsgRalation;
import com.neighbor.common.websoket.po.SocketMessage;

import java.util.HashMap;
import java.util.List;

public interface SocketMessageService {
    int deleteByPrimaryKey(Long msgId);

    int insertSelective(SocketMessage record);

    SocketMessage selectByPrimaryKey(Long msgId);

    int updateByPrimaryKeySelective(SocketMessage record);

	List<SocketMessage> selectByStatus(String status);

	List<SocketMessage> selectByTargetUserIdStatus(Long targetUserId, String status,String chatType);

	List<SocketMessage> selectbySelective(SocketMessage msg);

	void insertRelationShipSelective(GroupMsgRalation ralation);

    ResponseResult unreadRecord(UserInfo user);

    ResponseResult pageRecord(UserInfo user, Long targetUserId,Long msgId, PageTools pageTools);

    ResponseResult changeRecord(UserInfo user, Long targetUserId, Long msgId, String status);

    Long selectPageTotalCount(HashMap<?,?> map);

    List<SocketMessage> selectPageByObjectForList(HashMap<?,?> map);

	List<SocketMessage> selectForTargetUserMsgByStatus(String status, WebSocketChatType chatType);
	
	int deleteMessage(SocketMessage record);
	
    int jobDeleteMessage(SocketMessage record);

	List<SocketMessage> selectMsgByTargetGroupIdStatus(Long groupId, Long userId);

	ResponseResult groupPageRecord(UserInfo user, Long groupId, Long msgIdN, PageTools pageTools);
	
	void updateGroupMsgRalationStatusRecord(Long userId, Long msgId, Long groupId, MessageStatus status);

	void insertMsgBackup(SocketMessage msg);

	void updateWalletRefreshMsg(Long targetUserId);

	void walletRefreshNotice(Long sendUserId, Long groupMasterUId, String nickName);

	void groupRefreshNotice(Long sendUserId, Long targetUserId, String content);

	SocketMessage forceOfflineNoticeBuild();

	


	
}