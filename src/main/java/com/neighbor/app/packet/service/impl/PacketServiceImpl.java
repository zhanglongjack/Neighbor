package com.neighbor.app.packet.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.neighbor.app.balance.entity.BalanceDetail;
import com.neighbor.app.balance.po.TransactionSubTypeDesc;
import com.neighbor.app.balance.po.TransactionTypeDesc;
import com.neighbor.app.balance.service.BalanceDetailService;
import com.neighbor.app.packet.dao.PacketMapper;
import com.neighbor.app.packet.entity.Packet;
import com.neighbor.app.packet.service.PacketService;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.common.util.DateUtils;

@Service
public class PacketServiceImpl implements PacketService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private PacketMapper packetMapper;
	@Autowired
	private BalanceDetailService balanceDetailService;
	@Autowired
	private UserWalletService userWalletService;

	@Override
	public int insertSelective(Packet record) {
		return packetMapper.insertSelective(record);
	}

	@Override
	public Packet selectByPrimaryKey(Long id) {
		return packetMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Packet record) {
		return packetMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Packet sendPacket(Packet record) throws Exception {
		logger.info("开始处理发红包");
		record.setSendDate(DateUtils.getStringDateShort());
		record.setSendTime(DateUtils.getTimeShort());
		
		UserWallet userWallet = new UserWallet();
		userWallet.setuId(record.getUserId());
		userWallet.setAvailableAmount(record.getAmount());
		userWallet.setFreezeAmount(record.getAmount().negate());
		logger.info("减少钱包可用,增加冻结");
		userWalletService.updateWalletAmount(userWallet);
		UserWallet lastWallet = userWalletService.selectByPrimaryUserId(userWallet.getuId());
		// 转出交易明细
		BalanceDetail balanceDetailOut = new BalanceDetail();
		balanceDetailOut.setAmount(userWallet.getAvailableAmount());
		balanceDetailOut.setAvailableAmount(lastWallet.getAvailableAmount());
		balanceDetailOut.setuId(lastWallet.getuId());
		balanceDetailOut.setTransactionType(TransactionTypeDesc.payment.toString());
		balanceDetailOut.setTransactionSubType(TransactionSubTypeDesc.sendRedPack.toString());
		balanceDetailOut.setRemarks(TransactionSubTypeDesc.sendRedPack.getDes());
		balanceDetailOut.setTransactionId(lastWallet.getId());

		logger.info("保存交易信息");
		balanceDetailService.insertSelective(balanceDetailOut);
		logger.info("保存红包信息");
		packetMapper.insertSelective(record);
		logger.info("结束处 理发红包");
		return record;
	}

}