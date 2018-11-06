package com.neighbor.app.bankcard.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neighbor.app.bankcard.dao.BankCardMapper;
import com.neighbor.app.bankcard.entity.BankCard;
import com.neighbor.app.bankcard.service.BankCardService;

@Service
public class BankCardServiceImpl implements BankCardService{
	
	@Autowired
	private BankCardMapper bankCardMapper;

	@Override
	public int deleteByPrimaryKey(Long id) {
		return bankCardMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(BankCard record) {
		return bankCardMapper.insertSelective(record);
	}

	@Override
	public BankCard selectByPrimaryKey(Long id) {
		return bankCardMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(BankCard record) {
		return bankCardMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public BankCard selectBankCard(Long uId) {
		return bankCardMapper.selectBankCard(uId);
	}

	@Override
	public Long selectPageTotalCount(BankCard record) {
		return bankCardMapper.selectPageTotalCount(record);
	}

	@Override
	public List<BankCard> selectPageByObjectForList(BankCard record) {
		return bankCardMapper.selectPageByObjectForList(record);
	}

	@Override
	public List<BankCard> selectAll() {
		return bankCardMapper.selectAll();
	}
	
}
