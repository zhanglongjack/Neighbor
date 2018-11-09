package com.neighbor.app.wallet.dao;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.app.wallet.entity.UserWallet;

@Mapper
public interface UserWalletMapper {
	int deleteByPrimaryKey(Long id);

	int insertSelective(UserWallet record);

	UserWallet selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(UserWallet record);

	UserWallet selectByPrimaryUserId(Long userId);

}