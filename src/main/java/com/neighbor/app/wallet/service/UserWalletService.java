package com.neighbor.app.wallet.service;

import com.neighbor.app.wallet.entity.UserWallet;

public interface UserWalletService {
    int deleteByPrimaryKey(Long id);

    int insertSelective(UserWallet record);

    UserWallet selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserWallet record);
    int payPasswordEdit(UserWallet record);
	UserWallet selectByPrimaryUserId(Long userId);
    public UserWallet lockUserWalletByUserId(Long uid);
    
    int updateWalletAmount(UserWallet record);

}