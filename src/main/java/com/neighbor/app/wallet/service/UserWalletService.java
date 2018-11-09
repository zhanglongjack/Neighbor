package com.neighbor.app.wallet.service;

import com.neighbor.app.wallet.entity.UserWallet;

public interface UserWalletService {
    int deleteByPrimaryKey(Long id);

    int insertSelective(UserWallet record);

    UserWallet selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserWallet record);

	UserWallet selectByPrimaryUserId(Long userId);

}