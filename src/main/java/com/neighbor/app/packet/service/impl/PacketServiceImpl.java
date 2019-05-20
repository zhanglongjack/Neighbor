package com.neighbor.app.packet.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.neighbor.app.balance.entity.BalanceDetail;
import com.neighbor.app.balance.po.TransactionSubTypeDesc;
import com.neighbor.app.balance.po.TransactionTypeDesc;
import com.neighbor.app.balance.service.BalanceDetailService;
import com.neighbor.app.commission.entity.CommissionHandleTask;
import com.neighbor.app.commission.service.CommissionHandleTaskService;
import com.neighbor.app.game.constants.RuleTypeDesc;
import com.neighbor.app.game.entity.GameRule;
import com.neighbor.app.game.service.GameService;
import com.neighbor.app.group.entity.GroupMember;
import com.neighbor.app.group.service.GroupService;
import com.neighbor.app.packet.constants.PacketContainer;
import com.neighbor.app.packet.constants.PacketStatus;
import com.neighbor.app.packet.dao.PacketDetailMapper;
import com.neighbor.app.packet.dao.PacketMapper;
import com.neighbor.app.packet.entity.Packet;
import com.neighbor.app.packet.entity.PacketDetail;
import com.neighbor.app.packet.service.PacketService;
import com.neighbor.app.robot.entity.RobotConfig;
import com.neighbor.app.robot.service.RobotConfigService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.common.constants.CommonConstants;
import com.neighbor.common.constants.EnvConstants;
import com.neighbor.common.util.BigDecimalUtil;
import com.neighbor.common.util.DateUtils;
import com.neighbor.common.util.RedPackageUtil;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.service.SocketMessageService;

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
	private GameService gameService; 
	@Autowired
	private RobotConfigService robotConfigService; 
    @Autowired
    private CommonConstants commonConstants;
	@Autowired
	private CommissionHandleTaskService commissionHandleTaskService;
	@Autowired
	private SocketMessageService socketMessageService; 
	
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
	public Packet sendPacket(Packet record,UserWallet wallet,UserInfo user) throws Exception {
		if(wallet.getAvailableAmount().doubleValue()<record.getAmount().doubleValue()){
			return null;
		}
		logger.info("开始处理发红包");
		record.setSendDate(DateUtils.getStringDateShort());
		record.setSendTime(DateUtils.getTimeShort());
		record.setStatus(PacketStatus.uncollected.toString());
		record.setCollectedNum(0); 
		
		RobotConfig robot = robotConfigService.selectByPrimaryKey(Integer.parseInt(user.getRobotSno()));
		if(robot!=null){
			BigDecimal hitChance = getPacketConf(EnvConstants.PACKET_HIT_RATE);
			record.setHitChance(hitChance.doubleValue());
		}
		
		UserWallet userWallet = new UserWallet();
		userWallet.setuId(record.getUserId());
		userWallet.setAvailableAmount(record.getAmount().negate());
		
		logger.info("减少钱包可用");
		userWalletService.updateWalletAmount(userWallet);
		UserWallet lastWallet = userWalletService.selectByPrimaryUserId(userWallet.getuId());
		wallet.setAvailableAmount(lastWallet.getAvailableAmount());
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
		String teet = "1.02";
		System.out.println(Integer.parseInt(teet.charAt(teet.length()-1)+""));
		System.out.println(new BigDecimal(1).compareTo(new BigDecimal(2)));
		Long a = new Long(10);
		Long b = 10L;
		System.out.println(a == b);
	}
	
	private Packet computePacketRemain(Packet packet){
		List<PacketDetail> detailList = packet.getDetailList();
		double sum = 0;
		for(PacketDetail detail : detailList){
			sum+=detail.getGotAmount().doubleValue();
		}
		packet.remainMoney -= sum;
		packet.remainSize -= detailList.size();
		return packet;
	}

	private BigDecimal getPacketConf(String code){
		String str = commonConstants.getDictionarysBy(EnvConstants.PACKET_CONF, code);
		return new BigDecimal(str);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ResponseResult grabPacekt(Packet packet,UserInfo user,Long gameId) {
		logger.info("开始抢红包:"+packet);
		RobotConfig robot = null;
		if(user.getRobotSno()!=null){
			robot = robotConfigService.selectByPrimaryKey(Integer.parseInt(user.getRobotSno()));
			if(!robot.isGrap()){
				logger.info("机器人抢红包概率未中,结束");
				return new ResponseResult();
			}
			logger.info("机器人抢红包开始");
		}
		
		GroupMember member = groupService.selectGroupMemberBy(user.getId(), packet.getGroupId());
		Packet cachePacket = packetContainer.get(packet.getId());
		cachePacket = cachePacket!=null?cachePacket:packetMapper.selectByPrimaryKey(packet.getId());
		
		BigDecimal limitAmount = getPacketConf(EnvConstants.ROBOT_GRAP_LIMIT_AMOUNT);
		//Double limitAmount = Double.parseDouble(limitAmountStr);
		UserWallet lastWallet = userWalletService.selectByPrimaryUserId(user.getId());
		if(user.getRobotSno()!=null && lastWallet.getAvailableAmount().doubleValue()<limitAmount.doubleValue()){
			logger.info("机器人余额低于下线,本次不抢红包,机器人钱包:{}",lastWallet);
			return new ResponseResult();
		}
		if(lastWallet.getAvailableAmount().compareTo(cachePacket.getAmount())<0 && "0".equals(member.getMemberType())){
			
			ResponseResult resultResp = new ResponseResult();
			resultResp.addBody("packet", cachePacket);
			resultResp.setErrorCode(1);
			resultResp.setErrorMessage("余额不足");
			
			return resultResp;
		}
		
		ResponseResult resultResp = checLeftoverPacket(cachePacket.getStatus(),cachePacket,user.getId());
		if(resultResp.getErrorCode()!=0){
			return resultResp;
		}
		logger.info("开始锁表,红包id:{}",cachePacket.getId());
		Long packetId = packet.getId().longValue();
		synchronized (packetId) {
			Packet lockPacket = lockPacketByPrimaryKey(packetId);
			computePacketRemain(lockPacket);
			logger.info("红包编号:{},上锁后再检查实际是否还有剩余红包数:[{}]",lockPacket.getId(),lockPacket.remainSize);
			resultResp = checLeftoverPacket(lockPacket.getStatus(),lockPacket,user.getId());
			if(resultResp.getErrorCode()!=0){
				return resultResp;
			}
			
			logger.info("还有剩余红包数量[{}],开始处理",lockPacket.remainSize);

			double resultNum = RedPackageUtil.getRandomMoney(lockPacket, robot==null?true:robot.isHit());
			if(resultNum==0){
				logger.info("最后一个红包,机器人抢红包中雷概率未中,不抢");
				return  new ResponseResult();
			}
			
			PacketDetail detail = new PacketDetail();
			detail.setHeadUrl(user.getUserPhoto());
			detail.setNickName(user.getNickName());
			detail.setRemainMoney(new BigDecimal(lockPacket.remainMoney+""));
			detail.setRemainSize((long)lockPacket.remainSize);
			detail.setGotAmount(BigDecimalUtil.rounding(resultNum));
			String num = detail.getGotAmount().toPlainString();
			boolean isGotBomb = Integer.parseInt(num.substring(num.length()-1))==lockPacket.getHitNum();
			
			lockPacket.setCollectedNum(lockPacket.getCollectedNum() + 1);
			lockPacket.setStatus(lockPacket.getCollectedNum() == lockPacket.getPacketNum()?PacketStatus.collected.toString():PacketStatus.uncollected.toString());
			
			detail.setdPacketId(lockPacket.getId());
			detail.setGotUserId(user.getId());
			detail.setFree("1".equals(member.getMemberType()));
			detail.setGotBomb(isGotBomb);
			logger.info("是否需要分佣检查:"+detail);
			logger.info("用户检查:"+user);
			logger.info("群成员检查:"+member);

			lockPacket.addDetail(detail);
			packetDetailMapper.insertSelective(detail);
			logger.info("抢到的红包是[{}]尾数是[{}]信息:{},",num,Integer.parseInt(num.substring(num.length()-1)),detail);
			UserWallet senderWallet = userWalletService.selectByPrimaryUserId(lockPacket.getUserId());
			if(!detail.isFree()){
				// 处理踩雷
				handleHitBomb(detail.isGotBomb(), lockPacket, lastWallet,senderWallet);
				// 抢包中奖处理 
				handleLottery(gameId, lastWallet, detail.getGotAmount());
			}
			// 发包中奖检查和最佳标识检查处理(也可以优化给前端处理,减少压力)
			handleReward(gameId,lockPacket,senderWallet);
			// 抢红包处理
			grapPacketHandle(user, lastWallet, detail);
			
			packetMapper.updateByPrimaryKeySelective(lockPacket);
			
			packetContainer.put(lockPacket.getId(), lockPacket);
			
			CommissionHandleTask task=addCommissionTask(gameId, lockPacket, detail);
			
			resultResp.addBody("packet", lockPacket);
			resultResp.addBody("wallet", lastWallet);
			resultResp.addBody("task", task);
		
			logger.info("抢红包完成");
			return resultResp;
		}
	}

	private CommissionHandleTask addCommissionTask(Long gameId, Packet lockPacket, PacketDetail detail) {
		if( lockPacket.getUserId() != detail.getGotUserId() && detail.isFree()){
			logger.info("开始添加分佣任务记录");
			CommissionHandleTask task = new CommissionHandleTask();
			task.setGroupMasterUId(detail.getGotUserId());
			task.setUserId(lockPacket.getUserId());
			task.setSplitAmount(detail.getGotAmount());
			task.setGameId(gameId);
			commissionHandleTaskService.insert(task);
			//commisionHandleTaskQueue.offer(task); 
			//handleFreePacketCommission(detail,gameId,lockPacket.getUserId());
			return task;
		}
		return null;
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
		if(detail.isFree()){
			wallet.setFreezeAmount(detail.getGotAmount());
		}else{
			wallet.setAvailableAmount(detail.getGotAmount());
		}
		userWalletService.updateWalletAmount(wallet);
	}

	/**
	 * 发包中奖检查处理(也可以优化到定时任务中处理)
	 * @param lockPacket
	 * @param lastWallet
	 * @param detail
	 */
	private void handleLottery(long gameId, UserWallet graperWallet, BigDecimal grapAmount) {
		GameRule gameRule = gameService.ruleMatching(gameId, RuleTypeDesc.award, grapAmount.doubleValue()); 
		logger.info("中奖检查,抢包金额:{},游戏编号:{},游戏规则:{}",grapAmount.toPlainString(),gameId,gameRule);
		if(gameRule==null){
			return;
		}
		BigDecimal lotteryAmount = new BigDecimal(gameRule.getRuleValue());// 中奖金额 
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
	 * 发包中奖检查和最佳检查标识处理
	 * @param gameId 
	 * @param lockPacket
	 * @param senderWallet
	 */
	private void handleReward(Long gameId, Packet lockPacket, UserWallet senderWallet) {
		if(PacketStatus.collected == PacketStatus.valueOf(lockPacket.getStatus())){
			// 多雷奖励处理 
			List<PacketDetail>  detailList = lockPacket.getDetailList();
			logger.info("最佳检查开始:"+detailList.size());
			int maxIndex = 0;// 最佳下标
			int hitBombNum = 0;
			for(int i=0;i<detailList.size();i++){
				PacketDetail detail = detailList.get(i);
				logger.info("红包[{}]大于红包[{}]?",detail.getGotAmount().doubleValue(),detailList.get(maxIndex).getGotAmount().doubleValue());
				if(detail.getGotAmount().doubleValue()>detailList.get(maxIndex).getGotAmount().doubleValue()){
					maxIndex = i;
				}
				hitBombNum += detail.isGotBomb()?1:0;
			}
			logger.info("中雷数[{}],最佳位置[{}]",hitBombNum,maxIndex);
			logger.info("最佳明细List:",JSONArray.toJSON(detailList));
			if(maxIndex>=0){
				detailList.get(maxIndex).setMaximum(true);
				packetDetailMapper.updateByPrimaryKeySelective(detailList.get(maxIndex));
			}
			logger.info("最佳明细:",detailList.get(maxIndex));
			if(hitBombNum<=0){
				return ;
			}
			GameRule gameRule = gameService.ruleMatching(gameId, RuleTypeDesc.award, hitBombNum); // 中奖金额 
			if(gameRule==null){
				return;
			}
			BigDecimal amount = new BigDecimal(gameRule.getRuleValue()); // 多雷中奖奖励
			
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
	
	/**
	 * 后续可以优化,可以让队列和定时任务处理(这里只需要更新冻结金额后,后面就可以慢慢处理了)
	 * @param isGotBomb
	 * @param lockPacket
	 * @param graperWallet
	 * @param senderWallet
	 */
	private void handleHitBomb(boolean isGotBomb,Packet lockPacket, UserWallet graperWallet, UserWallet senderWallet) {
		logger.info("抢包人[{}]是否中雷检查:[{}]",graperWallet.getuId(),isGotBomb);
		if(isGotBomb){
			logger.info("抢包方踩雷处理");
//			BigDecimal packetHitRate = getPacketConf(EnvConstants.PACKET_HIT_RATE);
			// 中雷赔付金额
			BigDecimal amount = lockPacket.getAmount().multiply(lockPacket.getPaidRate());
			
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
			
			BigDecimal rewardScore = getPacketConf(EnvConstants.REWARD_SCORE);
			logger.info("保存抢包方踩雷交易信息");
			balanceDetailService.insertSelective(balanceDetail);
			UserWallet wallet = new UserWallet();
			wallet.setuId(graperWallet.getuId());
			wallet.setAvailableAmount(amount.negate());// 需要减少的金额
			wallet.setScore(rewardScore); // 需要增加的积分
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
			wallet1.setScore(rewardScore);// 需要增加的积分
			userWalletService.updateWalletAmount(wallet1);
		}
	}

	@Override
	public ResponseResult checLeftoverPacket(String statusStr,Packet packet,Long userId){
		ResponseResult resultResp = new ResponseResult();
		resultResp.addBody("packet", packet);
		PacketStatus status = PacketStatus.valueOf(statusStr);
		for(PacketDetail detail : packet.getDetailList()){
			if((long)detail.getGotUserId() == (long)userId){
				logger.info("红包被抢过了");
				resultResp.setErrorCode(3);
				resultResp.setErrorMessage("红包被抢过了");
				return resultResp;
			}
		}
		
		if(status != PacketStatus.collected&&status != PacketStatus.uncollected){
			logger.info("红包已过期");
			resultResp.setErrorCode(2);
			resultResp.setErrorMessage("红包已过期");
			return resultResp;
		}
		if(status == PacketStatus.collected || status == PacketStatus.uncollected && packet.getCollectedNum()== packet.getPacketNum()){
			logger.info("红包已抢完");
			resultResp.setErrorCode(4);
			resultResp.setErrorMessage("红包已抢完");
			return resultResp;
		}
		
		return resultResp;
		
	}
	
	@Override
	public ResponseResult packetDetailPage(PacketDetail packetDetail){
		Map<String,Object> gotSumPacketInfo = packetDetailMapper.queryGotPacketSumAmount(packetDetail);
		logger.info("gotSumPacketInfo===="+gotSumPacketInfo);
		Long size = packetDetailMapper.selectPageTotalCount(packetDetail);
		packetDetail.getPageTools().setTotal(size);
		List<PacketDetail> resultList = packetDetailMapper.selectPageByObjectForList(packetDetail);
		
		ResponseResult result = new ResponseResult();
		result.addBody("resultList", resultList);
		result.addBody("pageTools", packetDetail.getPageTools());
		result.addBody("dataInfo", gotSumPacketInfo);
		
		return result;
	}

	@Override
	public ResponseResult packetPage(Packet packet) {
		Map<String,Object> sendPacketInfo = packetMapper.querySendPacketSumAmount(packet);
		logger.info("gotSumPacketInfo===="+sendPacketInfo);
		Long size = packetMapper.selectPageTotalCount(packet);
		packet.getPageTools().setTotal(size);
		List<PacketDetail> resultList = packetMapper.selectPageByObjectForList(packet);
		
		ResponseResult result = new ResponseResult();
		result.addBody("resultList", resultList);
		result.addBody("pageTools", packet.getPageTools());
		result.addBody("dataInfo", sendPacketInfo);
		
		return result;
	}

	@Override
	public List<Packet> selectPacketBySelective(Packet packet) {
		return packetMapper.selectPacketBySelective(packet);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void expirePacketHandle(Long packetId) {
		Packet lockPacket = lockPacketByPrimaryKey(packetId);
		UserWallet lastWallet = userWalletService.selectByPrimaryUserId(lockPacket.getUserId());
		
		if(lockPacket.getCollectedNum()==0){
			lockPacket.setStatus(PacketStatus.all_back.toString());
		}else if(lockPacket.getCollectedNum()<lockPacket.getPacketNum()){
			lockPacket.setStatus(PacketStatus.part_back.toString());
		}else{
			logger.info("红包已领完:{}",lockPacket);
			return;
		}
		
		BigDecimal sumAmount = new BigDecimal(0);
		
		for(PacketDetail detail : lockPacket.getDetailList()){
			sumAmount = sumAmount.add(detail.getGotAmount());
		}
		
		BigDecimal backAmount = lockPacket.getAmount().subtract(sumAmount);
		lastWallet.setAvailableAmount(lastWallet.getAvailableAmount().add(backAmount));
		// 收红包交易明细
		BalanceDetail balanceDetail = new BalanceDetail();
		balanceDetail.setAmount(backAmount);
		balanceDetail.setAvailableAmount(lastWallet.getAvailableAmount());
		balanceDetail.setuId(lastWallet.getuId());
		balanceDetail.setTransactionType(TransactionTypeDesc.receipt.toString());
		balanceDetail.setTransactionSubType(TransactionSubTypeDesc.backRedPack.toString());
		balanceDetail.setRemarks(TransactionSubTypeDesc.backRedPack.getDes());
		balanceDetail.setTransactionId(lastWallet.getId());
		balanceDetailService.insertSelective(balanceDetail);
		
		UserWallet wallet = new UserWallet();
		wallet.setuId(lockPacket.getUserId());
		wallet.setAvailableAmount(backAmount);
		userWalletService.updateWalletAmount(wallet);
		
		packetMapper.updateByPrimaryKeySelective(lockPacket);
		packetContainer.put(lockPacket.getId(), lockPacket);
		
		logger.info("添加更新余额的消息通知"); 
		socketMessageService.walletRefreshNotice(null, lockPacket.getUserId(), "系统通知");
		
		logger.info("过期红包已处理退回结束,退回红包信息:{}",lockPacket);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}