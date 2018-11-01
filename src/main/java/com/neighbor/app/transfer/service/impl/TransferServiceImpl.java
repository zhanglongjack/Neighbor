package com.neighbor.app.transfer.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neighbor.app.transfer.dao.TransferMapper;
import com.neighbor.app.transfer.entity.Transfer;
import com.neighbor.app.transfer.service.TransferService;

@Service
public class TransferServiceImpl implements TransferService {

	@Autowired
	private TransferMapper transferMapper;
	@Override
	public List<Transfer> queryAll() throws Exception {
		// TODO Auto-generated method stub
		return transferMapper.selectAll();
	}

}
