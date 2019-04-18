package com.neighbor.app.wallet.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.neighbor.app.wallet.dao.UserWalletMapper;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;

@Service
public class UserWalletServiceImpl implements UserWalletService {

	@Autowired
	private UserWalletMapper userWalletMapper; 
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return userWalletMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(UserWallet record) {
		return userWalletMapper.insertSelective(record);
	}

	@Override
	public UserWallet selectByPrimaryKey(Long id) {
		return userWalletMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(UserWallet record) {
		return userWalletMapper.updateByPrimaryKeySelective(record);
	}

    @Override
    public int payPasswordEdit(UserWallet record) {
		return userWalletMapper.payPasswordEdit(record);
    }

    @Override
	public UserWallet selectByPrimaryUserId(Long userId) {
		return userWalletMapper.selectByPrimaryUserId(userId);
	}
	@Override
	public UserWallet lockUserWalletByUserId(Long uid) {
		return userWalletMapper.lockUserWalletByUserId(uid);
	}

	@Override
	public int updateWalletAmount(UserWallet record) {
		return userWalletMapper.updateWalletAmount(record);
	}

}