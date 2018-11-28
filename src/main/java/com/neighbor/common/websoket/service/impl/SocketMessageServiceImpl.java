package com.neighbor.common.websoket.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neighbor.common.websoket.dao.SocketMessageMapper;
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
		return selectbySelective(msg);
	}

	@Override
	public void insertRelationShipSelective(Map<String, Long> relationShip) {
		socketMessageMapper.insertRelationShipSelective(relationShip);
	} 

}