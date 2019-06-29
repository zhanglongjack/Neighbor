package com.neighbor.app.wallet.dao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.app.wallet.entity.UserWallet;

@Mapper
public interface UserWalletMapper {
	int deleteByPrimaryKey(Long id);

	int insertSelective(UserWallet record);

	UserWallet selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(UserWallet record);

	UserWallet selectByPrimaryUserId(Long userId);
    UserWallet lockUserWalletByUserId(Long uid);
	int payPasswordEdit(UserWallet record);

	int updateWalletAmount(UserWallet record);
	
	BigDecimal querySumLeftAmount();

}