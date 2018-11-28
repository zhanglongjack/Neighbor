package com.neighbor.app.wallet.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.neighbor.app.balance.entity.BalanceDetail;
import com.neighbor.app.balance.po.TransactionItemDesc;
import com.neighbor.app.balance.po.TransactionSubTypeDesc;
import com.neighbor.app.balance.po.TransactionTypeDesc;
import com.neighbor.app.balance.service.BalanceDetailService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.wallet.dao.UserWalletMapper;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.common.util.StringUtil;

@Service
public class UserWalletServiceImpl implements UserWalletService {

	@Autowired
	private UserWalletMapper userWalletMapper;
    @Autowired
    private BalanceDetailService balanceDetailService;
    
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

	@Override
	@Transactional(readOnly = false,rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
	public UserWallet sendPacket(UserWallet userWallet) {
		updateWalletAmount(userWallet);
		UserWallet lastWallet = userWalletMapper.selectByPrimaryUserId(userWallet.getuId());
		//转出交易明细
		BalanceDetail balanceDetailOut = new BalanceDetail();
		balanceDetailOut.setAmount(userWallet.getAvailableAmount());
		balanceDetailOut.setAvailableAmount(lastWallet.getAvailableAmount());
		balanceDetailOut.setuId(lastWallet.getuId());
		balanceDetailOut.setTransactionType(TransactionTypeDesc.payment.toString());
		balanceDetailOut.setTransactionSubType(TransactionSubTypeDesc.sendRedPack.toString());
		balanceDetailOut.setRemarks(TransactionSubTypeDesc.sendRedPack.toString());
		balanceDetailOut.setTransactionId(lastWallet.getId());
		
		balanceDetailService.insertSelective(balanceDetailOut);
		return lastWallet;
	}
 

}