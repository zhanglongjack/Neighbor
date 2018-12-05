package com.neighbor.common.websoket.service.impl;

import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.constants.MessageStatus;
import com.neighbor.common.websoket.dao.SocketMessageMapper;
import com.neighbor.common.websoket.po.SocketMessage;
import com.neighbor.common.websoket.service.SocketMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		return selectbySelective(msg);
	}

	@Override
	public void insertRelationShipSelective(Map<String, Long> relationShip) {
		socketMessageMapper.insertRelationShipSelective(relationShip);
	}


	@Override
	public ResponseResult unreadRecord(UserInfo user) {
		ResponseResult responseResult = new ResponseResult();
		SocketMessage socketMessage = new SocketMessage();
		socketMessage.setTargetUserId(user.getId());
		socketMessage.setStatus(MessageStatus.pushed_response.toString());//未推送状态
		List<SocketMessage> socketMessageList = selectbySelective(socketMessage);
		responseResult.addBody("messageList",socketMessageList);
		return responseResult;
	}

	@Override
	public ResponseResult pageRecord(UserInfo user, Long friendId,Long msgId, PageTools pageTools) {
		HashMap map = new HashMap();
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
		HashMap map = new HashMap();
		map.put("userId",user.getId());
		map.put("friendId",friendId);
		map.put("msgId",msgId);
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
	public Long selectPageTotalCount(HashMap map) {
		return socketMessageMapper.selectPageTotalCount(map);
	}

	@Override
	public List<SocketMessage> selectPageByObjectForList(HashMap map) {
		return socketMessageMapper.selectPageByObjectForList(map);
	}
}