package com.neighbor.app.balance.service.impl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neighbor.app.balance.dao.BalanceDetailMapper;
import com.neighbor.app.balance.entity.BalanceDetail;
import com.neighbor.app.balance.service.BalanceDetailService;

@Service
public class BalanceDetailServiceImpl implements BalanceDetailService {

	@Autowired
	private BalanceDetailMapper balanceDetailMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return balanceDetailMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(BalanceDetail record) {
		return balanceDetailMapper.insertSelective(record);
	}

	@Override
	public BalanceDetail selectByPrimaryKey(Long id) {
		return balanceDetailMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(BalanceDetail record) {
		return balanceDetailMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public Long selectPageTotalCount(BalanceDetail record) {
		return balanceDetailMapper.selectPageTotalCount(record);
	}

	@Override
	public List<BalanceDetail> selectPageByObjectForList(BalanceDetail record) {
		return balanceDetailMapper.selectPageByObjectForList(record);
	}

	@Override
	public List<BalanceDetail> selectAll() {
		return balanceDetailMapper.selectAll();
	} 
	
}