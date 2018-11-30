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
import com.neighbor.app.packet.constants.PacketContainer;
import com.neighbor.app.packet.constants.PacketStatus;
import com.neighbor.app.packet.dao.PacketDetailMapper;
import com.neighbor.app.packet.dao.PacketMapper;
import com.neighbor.app.packet.entity.Packet;
import com.neighbor.app.packet.entity.PacketDetail;
import com.neighbor.app.packet.service.PacketService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.common.util.DateUtils;
import com.neighbor.common.util.ResponseResult;

@Service
public class PacketServiceImpl implements PacketService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private PacketMapper packetMapper;
	@Autowired
	private PacketDetailMapper packetDetailMapper;
	@Autowired
	private PacketContainer packetContainer;
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
		record.setStatus(PacketStatus.uncollected.toString());
		UserWallet userWallet = new UserWallet();
		userWallet.setuId(record.getUserId());
		userWallet.setAvailableAmount(record.getAmount().negate());
//		userWallet.setFreezeAmount(record.getAmount().negate());
		logger.info("减少钱包可用");
		userWalletService.updateWalletAmount(userWallet);
		UserWallet lastWallet = userWalletService.selectByPrimaryUserId(userWallet.getuId());
		// 发红包交易明细
		BalanceDetail balanceDetail = new BalanceDetail();
		balanceDetail.setAmount(record.getAmount().negate());
		balanceDetail.setAvailableAmount(lastWallet.getAvailableAmount());
		balanceDetail.setuId(lastWallet.getuId());
		balanceDetail.setTransactionType(TransactionTypeDesc.payment.toString());
		balanceDetail.setTransactionSubType(TransactionSubTypeDesc.sendRedPack.toString());
		balanceDetail.setRemarks(TransactionSubTypeDesc.sendRedPack.getDes());
		balanceDetail.setTransactionId(lastWallet.getId());

		logger.info("保存交易信息");
		balanceDetailService.insertSelective(balanceDetail);
		
		logger.info("保存红包信息");
		packetMapper.insertSelective(record);
		packetContainer.put(record.getId(), record);
		logger.info("结束处 理发红包");
		return record;
	}


	@Override
	public Packet lockPacketByPrimaryKey(Long id) {
		return packetMapper.lockPacketByPrimaryKey(id);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ResponseResult grabPacekt(Packet packet,UserInfo user) {
		Packet cachePacket = packetContainer.get(packet.getId());
		ResponseResult resultResp = checLeftoverPacket(cachePacket.getStatus(),cachePacket);
		if(resultResp.getErrorCode()!=0){
			return resultResp;
		}
		
		logger.info("上锁再检查实际是否还有剩余红包");
		Packet lockPacket = lockPacketByPrimaryKey(cachePacket.getId());
		resultResp = checLeftoverPacket(lockPacket.getStatus(),lockPacket);
		if(resultResp.getErrorCode()!=0){
			return resultResp;
		}
		logger.info("还有剩余红包,开始处理");
		PacketDetail detail = new PacketDetail();
		if(lockPacket.getGroupId()!=null && lockPacket.getCollectedNum()+1== lockPacket.getPacketNum()){
			// 由系统抢
			
			// 然后修改状态为抢完
			lockPacket.setCollectedNum(lockPacket.getPacketNum());
			lockPacket.setStatus(PacketStatus.collected.toString());
		}else if (lockPacket.getReceiveUserId()!=null){
			lockPacket.setStatus(PacketStatus.collected.toString());
			detail.setGotAmount(lockPacket.getAmount());
		}else{
			lockPacket.setCollectedNum(lockPacket.getCollectedNum() + 1);
		}
		
		detail.setdPacketId(lockPacket.getId());
		detail.setGotUserId(user.getId());
		
		lockPacket.addDetail(detail);
		packetMapper.updateByPrimaryKeySelective(lockPacket);
		packetDetailMapper.insertSelective(detail);
		
		UserWallet wallet = new UserWallet();
		wallet.setuId(user.getId());
		wallet.setAvailableAmount(detail.getGotAmount());
		userWalletService.updateWalletAmount(wallet);
		
		UserWallet lastWallet = userWalletService.selectByPrimaryUserId(wallet.getuId());
		// 收红包交易明细
		BalanceDetail balanceDetail = new BalanceDetail();
		balanceDetail.setAmount(detail.getGotAmount());
		balanceDetail.setAvailableAmount(lastWallet.getAvailableAmount());
		balanceDetail.setuId(lastWallet.getuId());
		balanceDetail.setTransactionType(TransactionTypeDesc.receipt.toString());
		balanceDetail.setTransactionSubType(TransactionSubTypeDesc.receiveRedPack.toString());
		balanceDetail.setRemarks(TransactionSubTypeDesc.receiveRedPack.getDes());
		balanceDetail.setTransactionId(lastWallet.getId());

		logger.info("保存交易信息");
		balanceDetailService.insertSelective(balanceDetail);
		
		packetContainer.put(lockPacket.getId(), lockPacket);
		resultResp.addBody("packet", lockPacket);
		resultResp.addBody("wallet", lastWallet);
		
		logger.info("抢红包完成");
		return resultResp;
	}

	@Override
	public ResponseResult checLeftoverPacket(String statusStr,Packet packet){
		ResponseResult resultResp = new ResponseResult();
		resultResp.addBody("packet", packet);
		PacketStatus status = PacketStatus.valueOf(statusStr);
		
		if(status != PacketStatus.collected&&status != PacketStatus.uncollected){
			logger.info("红包已过期");
			resultResp.setErrorCode(2);
			resultResp.setErrorMessage("红包已过期");
			return resultResp;
		}
		if(status == PacketStatus.collected || status == PacketStatus.uncollected && packet.getCollectedNum()== packet.getPacketNum()){
			logger.info("红包已抢完");
			resultResp.setErrorCode(1);
			resultResp.setErrorMessage("红包已抢完");
			return resultResp;
		}
		
		return resultResp;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}