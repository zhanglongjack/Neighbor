package com.neighbor.app.wallet.service.impl;

import com.neighbor.app.balance.dao.BalanceDetailMapper;
import com.neighbor.app.balance.entity.BalanceDetail;
import com.neighbor.app.balance.po.TransactionSubTypeDesc;
import com.neighbor.app.balance.po.TransactionTypeDesc;
import com.neighbor.app.wallet.dao.UserWalletMapper;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserWalletServiceImpl implements UserWalletService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private BalanceDetailMapper balanceDetailMapper;
	@Autowired
	private UserWalletMapper userWalletMapper;

	@Autowired
	private Environment env;
	
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

	public void sysUserChangeWallet(String transactionType, BigDecimal amount, TransactionSubTypeDesc transactionSubType) {
		Long sysUserId = Long.parseLong(env.getProperty("sys.user.id"));
		commonChangeWallet(transactionType, amount, transactionSubType, sysUserId);
	}

	private void commonChangeWallet(String transactionType, BigDecimal amount, TransactionSubTypeDesc transactionSubType, Long sysUserId) {
		UserWallet lastWallet = this.selectByPrimaryUserId(sysUserId); //超管的钱包
		if(TransactionTypeDesc.expenditure.toString().equals(transactionType)||TransactionTypeDesc.payment.toString().equals(transactionType)){
			amount = amount.negate();
		}
		logger.info("账户:"+sysUserId+","+transactionSubType.getDes()+TransactionTypeDesc.getDesByValue(transactionType)+"交易信息");
		lastWallet.setAvailableAmount(lastWallet.getAvailableAmount().add(amount));
		// 发包雷中交易明细
		BalanceDetail bossBalanceDetail = new BalanceDetail();
		bossBalanceDetail.setAmount(amount);
		bossBalanceDetail.setAvailableAmount(lastWallet.getAvailableAmount());
		bossBalanceDetail.setuId(lastWallet.getuId());
		bossBalanceDetail.setTransactionType(transactionType);
		bossBalanceDetail.setTransactionSubType(transactionSubType.toString());
		bossBalanceDetail.setRemarks(transactionSubType.getDes());
		bossBalanceDetail.setTransactionId(lastWallet.getId());

		balanceDetailMapper.insertSelective(bossBalanceDetail);
		logger.info("修改账户:"+sysUserId+",钱包");
		UserWallet updateW = new UserWallet();
		updateW.setuId(lastWallet.getuId());
		updateW.setAvailableAmount(amount);
		this.updateWalletAmount(updateW);
	}


	@Override
	public void updateRobotWallet(Long userId, String action, BigDecimal amount) {
		String transactionType = TransactionTypeDesc.income.toString();
		TransactionSubTypeDesc transactionSubType = TransactionSubTypeDesc.robotIn;
		if("2".equals(action)){
			transactionType = TransactionTypeDesc.expenditure.toString();
			transactionSubType = TransactionSubTypeDesc.robotOut;
		}
		commonChangeWallet(transactionType,amount,transactionSubType,userId);
	}
}