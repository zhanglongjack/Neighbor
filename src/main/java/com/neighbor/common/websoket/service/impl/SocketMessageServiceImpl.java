package com.neighbor.common.websoket.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.constants.MessageDeleteStates;
import com.neighbor.common.websoket.constants.MessageStatus;
import com.neighbor.common.websoket.dao.SocketMessageMapper;
import com.neighbor.common.websoket.po.GroupMsgRalation;
import com.neighbor.common.websoket.po.SocketMessage;
import com.neighbor.common.websoket.service.SocketMessageService;

@Service
public class SocketMessageServiceImpl implements SocketMessageService {

	@Autowired
	private SocketMessageMapper socketMessageMapper;
	
	@Override
	public int deleteByPrimaryKey(Long msgId) {
		return socketMessageMapper.deleteByPrimaryKey(msgId);
	}

	@Override
	public int insertSelective(SocketMessage record) {
		return socketMessageMapper.insertSelective(record);
	}

	@Override
	public SocketMessage selectByPrimaryKey(Long msgId) {
		return socketMessageMapper.selectByPrimaryKey(msgId);
	}

	@Override
	public int updateByPrimaryKeySelective(SocketMessage record) {
		return socketMessageMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<SocketMessage> selectByStatus(String status) {
		return socketMessageMapper.selectByStatus(status);
	}

	@Override
	public List<SocketMessage> selectbySelective(SocketMessage msg) { 
		return socketMessageMapper.selectbySelective(msg);
	} 
	
	@Override
	public List<SocketMessage> selectByTargetUserIdStatus(Long targetUserId, String status, String chatType) {
		SocketMessage msg = new SocketMessage();
		msg.setTargetUserId(targetUserId);
		msg.setStatus(status);
		msg.setChatType(chatType);
		msg.setTargetUserDeleteFlag(MessageDeleteStates.normal.getDes());
		return selectbySelective(msg);
	}

	@Override
	public void insertRelationShipSelective(GroupMsgRalation ralation) {
		socketMessageMapper.insertRelationShipSelective(ralation);
	}


	@Override
	public ResponseResult unreadRecord(UserInfo user) {
		ResponseResult responseResult = new ResponseResult();
		SocketMessage socketMessage = new SocketMessage();
		socketMessage.setTargetUserId(user.getId());
		socketMessage.setTargetUserDeleteFlag(MessageDeleteStates.normal.getDes());
		socketMessage.setMasterMsgType("1");
		socketMessage.setStatus(MessageStatus.pushed_response.toString());//未推送状态
		List<SocketMessage> socketMessageList = selectbySelective(socketMessage);
		responseResult.addBody("messageList",socketMessageList);
		return responseResult;
	}

	@Override
	public ResponseResult pageRecord(UserInfo user, Long friendId,Long msgId, PageTools pageTools) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("userId",user.getId());
		map.put("friendId",friendId);
		map.put("msgId",msgId);
		map.put("rowIndex",pageTools.getRowIndex());
		map.put("pageSize",pageTools.getPageSize());
		Long size = selectPageTotalCount(map);
		pageTools.setTotal(size);
		List<SocketMessage> messageList = selectPageByObjectForList(map);
		ResponseResult result = new ResponseResult();
		result.addBody("messageList", messageList);
		result.addBody("pageTools", pageTools);
		return result;
	}

	@Override
	public ResponseResult changeRecord(UserInfo user, Long friendId, Long msgId, String status) {
		ResponseResult result = new ResponseResult();
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("userId",user.getId());
		map.put("friendId",friendId);
		map.put("msgId",msgId);
		map.put("masterMsgType","1");
		if(MessageStatus.complete.toString().equals(status)){
			map.put("status",MessageStatus.complete.toString());
			map.put("changeComplete","ok");
		}else if(MessageStatus.pushed.toString().equals(status)){
			map.put("status",MessageStatus.pushed.toString());
			map.put("changePushed","ok");
		}
		socketMessageMapper.changeRecord(map);
		return result;
	}

	@Override
	public Long selectPageTotalCount(HashMap<?,?> map) {
		return socketMessageMapper.selectPageTotalCount(map);
	}

	@Override
	public List<SocketMessage> selectPageByObjectForList(HashMap<?,?> map) {
		return socketMessageMapper.selectPageByObjectForList(map);
	}

	@Override
	public List<SocketMessage> selectForTargetUserMsgByStatus(String status) {
		SocketMessage msg = new SocketMessage();
		msg.setStatus(status);
		msg.setTargetUserNotNull(1);
//		msg.setMasterMsgType("1");
		return selectbySelective(msg);
	}

	@Override
	public int deleteMessage(SocketMessage record) {
		return socketMessageMapper.deleteMessage(record);
	}

	@Override
	public int jobDeleteMessage(SocketMessage record) {
		return socketMessageMapper.jobDeleteMessage(record);
	}

	@Override
	public List<SocketMessage> selectMsgByTargetGroupIdStatus(Long groupId, Long userId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("targetGroupId", groupId);
		map.put("userId", userId);
		return socketMessageMapper.selectMsgByTargetGroupIdStatus(map);
	}

	@Override
	public ResponseResult groupPageRecord(UserInfo user, Long groupId ,Long msgIdN, PageTools pageTools) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("userId",user.getId());
		map.put("msgId",msgIdN);
		map.put("targetGroupId",groupId);
		map.put("rowIndex",pageTools.getRowIndex());
		map.put("pageSize",pageTools.getPageSize());
		Long size = selectGroupPageTotalCount(map);
		pageTools.setTotal(size);
		List<SocketMessage> messageList = selectGroupPageByObjectForList(map);
		ResponseResult result = new ResponseResult();
		result.addBody("messageList", messageList);
		result.addBody("pageTools", pageTools);
		return result;
	}

	private List<SocketMessage> selectGroupPageByObjectForList(HashMap<String, Object> map) {
		return socketMessageMapper.selectGroupPageByObjectForList(map);
	}

	private Long selectGroupPageTotalCount(HashMap<String, Object> map) {
		return socketMessageMapper.selectGroupPageTotalCount(map);
	}

	@Override
	public void updateGroupMsgRalationStatusRecord(Long userId,Long msgId,Long groupId,MessageStatus status) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId); 
		map.put(MessageStatus.complete == status?"msgIdGreater":"msgId",msgId);
		map.put("targetGroupId", groupId);
		map.put("status", status+"");
		
		socketMessageMapper.updateGroupMsgRalationStatusRecord(map);
	}
}