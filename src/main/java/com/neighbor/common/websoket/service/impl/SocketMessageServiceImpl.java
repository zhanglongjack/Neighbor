package com.neighbor.common.websoket.service.impl;

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

}