package com.neighbor.app.packet.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.neighbor.app.balance.entity.BalanceDetail;
import com.neighbor.app.balance.po.TransactionSubTypeDesc;
import com.neighbor.app.balance.po.TransactionTypeDesc;
import com.neighbor.app.balance.service.BalanceDetailService;
import com.neighbor.app.group.entity.GroupMember;
import com.neighbor.app.group.service.GroupService;
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
import com.neighbor.common.constants.EnvConstants;
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
	@Autowired
	private GroupService groupService;
    @Autowired
    private Environment env;
    
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
	
	public static double getRandomMoney(Packet packet) {
	    // remainSize 剩余的红包数量
	    // remainMoney 剩余的钱
	    if (packet.getPacketNum() == 1) {
	    	packet.setPacketNum(packet.getPacketNum()-1);
	        return (double) Math.round(packet.getAmount().doubleValue() * 100) / 100;
	    }
	    Random r     = new Random();
	    double min   = 0.01; //
	    double max   = packet.getAmount().doubleValue()/packet.getPacketNum().intValue()*2;
	    double money = r.nextDouble() * max;
	    money = money <= min ? 0.01: money;
	    money = Math.floor(money * 100) / 100;
	    packet.setPacketNum(packet.getPacketNum()-1);
	    packet.setAmount(packet.getAmount().subtract(new BigDecimal(money)));
	    return money;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Packet sendPacket(Packet record) throws Exception {
		logger.info("开始处理发红包");
		record.setSendDate(DateUtils.getStringDateShort());
		record.setSendTime(DateUtils.getTimeShort());
		record.setStatus(PacketStatus.uncollected.toString());
		String str = "";
		Packet packetGenerate = new Packet();
		packetGenerate.setAmount(record.getAmount());
		packetGenerate.setPacketNum(record.getPacketNum());
		for(int i=record.getPacketNum();i>0;i--){
			str+=","+getRandomMoney(packetGenerate);
		}
		record.setRandomAmount(str.substring(1));
		
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
		logger.info("结束处 理发红包"+record);
		return record;
	}


	@Override
	public Packet lockPacketByPrimaryKey(Long id) {
		return packetMapper.lockPacketByPrimaryKey(id);
	}
	
	public static void main(String[] args) {
		System.out.println(new BigDecimal(1).compareTo(new BigDecimal(2)));
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ResponseResult grabPacekt(Packet packet,UserInfo user) {
		logger.info("开始抢红包"+packet);

		GroupMember member = groupService.selectGroupMemberBy(user.getId(), packet.getGroupId());
		Packet cachePacket = packetContainer.get(packet.getId());
		cachePacket = cachePacket!=null?cachePacket:packetMapper.selectByPrimaryKey(packet.getId());
		
		UserWallet lastWallet = userWalletService.selectByPrimaryUserId(user.getId());
		if(lastWallet.getAvailableAmount().compareTo(cachePacket.getAmount())<0 && "0".equals(member.getMemberType())){
			
			ResponseResult resultResp = new ResponseResult();
			resultResp.addBody("packet", cachePacket);
			resultResp.setErrorCode(1);
			resultResp.setErrorMessage("余额不足");
			
			return resultResp;
			
		}
		
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
//		if(lockPacket.getGroupId()!=null && lockPacket.getCollectedNum()+1== lockPacket.getPacketNum()){
//			// 由系统抢
//			detail.setIsFree("1");
//			// 然后修改状态为抢完
//			lockPacket.setCollectedNum(lockPacket.getPacketNum());
//			lockPacket.setStatus(PacketStatus.collected.toString());
//		}else{
//			
//		}
		
		String randomAmounts[] = lockPacket.getRandomAmountList();
		detail.setGotAmount(new BigDecimal(randomAmounts[lockPacket.getCollectedNum()]));
		lockPacket.setCollectedNum(lockPacket.getCollectedNum() + 1);
		lockPacket.setStatus(lockPacket.getCollectedNum() == lockPacket.getPacketNum()?PacketStatus.collected.toString():PacketStatus.uncollected.toString());
		String num = detail.getGotAmount().toPlainString();
		
		detail.setdPacketId(lockPacket.getId());
		detail.setGotUserId(user.getId());
		detail.setFree("1".equals(member.getMemberType()));
		detail.setGotBomb(num.charAt(num.length()-1)==lockPacket.getHitNum());
		
		UserWallet senderWallet = userWalletService.selectByPrimaryUserId(packet.getUserId());
		// 处理踩雷
		handleHitBomb(detail.isGotBomb(), lockPacket.getAmount(), lastWallet,senderWallet);
		// 抢包中奖处理 
		handleLottery( lastWallet, detail.getGotAmount());
		// 发包中奖检查和最佳检查处理
		handleReward(lockPacket,senderWallet);
		// 抢红包处理
		grapPacketHandle(user, lastWallet, detail);
		
		lockPacket.addDetail(detail);
		packetMapper.updateByPrimaryKeySelective(lockPacket);
		packetDetailMapper.insertSelective(detail);
		
		packetContainer.put(lockPacket.getId(), lockPacket);
		resultResp.addBody("packet", lockPacket);
		resultResp.addBody("wallet", lastWallet);
		
		logger.info("抢红包完成");
		return resultResp;
	}

	private void grapPacketHandle(UserInfo user, UserWallet lastWallet, PacketDetail detail) {
		lastWallet.setAvailableAmount(lastWallet.getAvailableAmount().add(detail.getGotAmount()));
		
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
		
		UserWallet wallet = new UserWallet();
		wallet.setuId(user.getId());
		wallet.setAvailableAmount(detail.getGotAmount());
		userWalletService.updateWalletAmount(wallet);
	}

	/**
	 * 发包中奖检查处理
	 * @param lockPacket
	 * @param lastWallet
	 * @param detail
	 */
	private void handleLottery( UserWallet graperWallet, BigDecimal grapAmount) {
		BigDecimal lotteryAmount = new BigDecimal(0); // 中奖金额 TODO
		if(lotteryAmount.doubleValue() <= 0){
			return;
		}
		logger.info("抢包中奖处理");
		graperWallet.setAvailableAmount(graperWallet.getAvailableAmount().subtract(lotteryAmount));
		// 收红包交易明细
		BalanceDetail balanceDetail = new BalanceDetail();
		balanceDetail.setAmount(lotteryAmount);
		balanceDetail.setAvailableAmount(graperWallet.getAvailableAmount());
		balanceDetail.setuId(graperWallet.getuId());
		balanceDetail.setTransactionType(TransactionTypeDesc.receipt.toString());
		balanceDetail.setTransactionSubType(TransactionSubTypeDesc.lucked.toString());
		balanceDetail.setRemarks(TransactionSubTypeDesc.lucked.getDes());
		balanceDetail.setTransactionId(graperWallet.getId());
		
		logger.info("保存抢包方中奖交易信息");
		balanceDetailService.insertSelective(balanceDetail);
		UserWallet wallet = new UserWallet();
		wallet.setuId(graperWallet.getuId());
		wallet.setAvailableAmount(lotteryAmount);// 需要增加的金额
		userWalletService.updateWalletAmount(wallet);
		
	}

	/**
	 * 发包中奖检查和最佳检查处理
	 * @param lockPacket
	 * @param senderWallet
	 */
	private void handleReward(Packet lockPacket, UserWallet senderWallet) {
		if(PacketStatus.collected == PacketStatus.valueOf(lockPacket.getStatus())){
			// 多雷奖励处理 TODO
			List<PacketDetail> detailList = lockPacket.getDetailList();
			logger.info("中奖检查开始"+detailList.size());
			int maxIndex = -1;// 最佳下标
			int hitBombNum = 0;
			for(int i=0;i<detailList.size();i++){
				PacketDetail detail = detailList.get(i);
				if(detail.getGotAmount().doubleValue()>detailList.get(maxIndex).getGotAmount().doubleValue()){
					maxIndex = i;
				}
				hitBombNum += detail.isGotBomb()?1:0;
			}
			logger.info("中雷数[{}],最佳位置[{}]",hitBombNum,maxIndex);
			if(maxIndex>=0){
				detailList.get(maxIndex).setMaximum(true);
			}
			
			if(hitBombNum<=0){
				return ;
			}
			
			BigDecimal amount = new BigDecimal(0); // TODO 多雷中奖
			if(amount.doubleValue()<=0){
				return;
			}
			
			logger.info("发包方多雷中奖收款");
			senderWallet.setAvailableAmount(senderWallet.getAvailableAmount().add(amount));
			// 发包雷中交易明细
			BalanceDetail senderBalanceDetail = new BalanceDetail();
			senderBalanceDetail.setAmount(amount);
			senderBalanceDetail.setAvailableAmount(senderWallet.getAvailableAmount());
			senderBalanceDetail.setuId(senderWallet.getuId());
			senderBalanceDetail.setTransactionType(TransactionTypeDesc.receipt.toString());
			senderBalanceDetail.setTransactionSubType(TransactionSubTypeDesc.lucked.toString());
			senderBalanceDetail.setRemarks(TransactionSubTypeDesc.lucked.getDes());
			senderBalanceDetail.setTransactionId(senderWallet.getId());
			
			logger.info("保存发包方多雷中奖收款交易信息");
			balanceDetailService.insertSelective(senderBalanceDetail);
			UserWallet wallet1 = new UserWallet();
			wallet1.setuId(senderWallet.getuId());
			wallet1.setAvailableAmount(amount);// 需要增加的金额
			userWalletService.updateWalletAmount(wallet1);
			
		}
	}

	private void handleHitBomb(boolean isGotBomb,BigDecimal packetAmount, UserWallet graperWallet, UserWallet senderWallet) {
		if(isGotBomb){
			logger.info("抢包方踩雷处理");
			// 中雷赔付金额
			BigDecimal amount = packetAmount.multiply(new BigDecimal(env.getProperty(EnvConstants.PACKET_HIT_RATE)));
			
			graperWallet.setAvailableAmount(graperWallet.getAvailableAmount().subtract(amount));
			// 收红包交易明细
			BalanceDetail balanceDetail = new BalanceDetail();
			balanceDetail.setAmount(amount.negate());
			balanceDetail.setAvailableAmount(graperWallet.getAvailableAmount());
			balanceDetail.setuId(graperWallet.getuId());
			balanceDetail.setTransactionType(TransactionTypeDesc.payment.toString());
			balanceDetail.setTransactionSubType(TransactionSubTypeDesc.thunderOut.toString());
			balanceDetail.setRemarks(TransactionSubTypeDesc.thunderOut.getDes());
			balanceDetail.setTransactionId(graperWallet.getId());
			
			logger.info("保存抢包方踩雷交易信息");
			balanceDetailService.insertSelective(balanceDetail);
			UserWallet wallet = new UserWallet();
			wallet.setuId(graperWallet.getuId());
			wallet.setAvailableAmount(amount.negate());// 需要减少的金额
			wallet.setScore(new BigDecimal(1)); // 需要增加的积分
			userWalletService.updateWalletAmount(wallet);
			
			logger.info("发包方中雷收款");
			senderWallet.setAvailableAmount(senderWallet.getAvailableAmount().add(amount));
			// 发包雷中交易明细
			BalanceDetail senderBalanceDetail = new BalanceDetail();
			senderBalanceDetail.setAmount(amount);
			senderBalanceDetail.setAvailableAmount(senderWallet.getAvailableAmount());
			senderBalanceDetail.setuId(senderWallet.getuId());
			senderBalanceDetail.setTransactionType(TransactionTypeDesc.receipt.toString());
			senderBalanceDetail.setTransactionSubType(TransactionSubTypeDesc.thunderIn.toString());
			senderBalanceDetail.setRemarks(TransactionSubTypeDesc.thunderIn.getDes());
			senderBalanceDetail.setTransactionId(senderWallet.getId());
			
			logger.info("保存发包方中雷收款交易信息");
			balanceDetailService.insertSelective(senderBalanceDetail);
			UserWallet wallet1 = new UserWallet();
			wallet1.setuId(senderWallet.getuId());
			wallet1.setAvailableAmount(amount);// 需要增加的金额
			wallet1.setScore(new BigDecimal(1));// 需要增加的积分
			userWalletService.updateWalletAmount(wallet1);
		}
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