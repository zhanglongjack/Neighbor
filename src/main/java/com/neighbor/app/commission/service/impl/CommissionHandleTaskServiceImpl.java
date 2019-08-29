package com.neighbor.app.commission.service.impl;

import java.math.BigDecimal;
import java.util.List;

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
import com.neighbor.app.commission.dao.CommissionHandleTaskMapper;
import com.neighbor.app.commission.entity.CommissionHandleTask;
import com.neighbor.app.commission.entity.UserCommission;
import com.neighbor.app.commission.service.CommissionHandleTaskService;
import com.neighbor.app.commission.service.CommissionService;
import com.neighbor.app.game.constants.RuleTypeDesc;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.common.constants.CommonConstants;
import com.neighbor.common.constants.EnvConstants;
import com.neighbor.common.util.BigDecimalUtil;
import com.neighbor.common.util.DateUtils;
import com.neighbor.common.websoket.service.SocketMessageService;

@Service
public class CommissionHandleTaskServiceImpl implements CommissionHandleTaskService  {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private CommissionHandleTaskMapper commissionHandleTaskMapper;
	@Autowired
	private CommissionService commissionService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserWalletService userWalletService;
	@Autowired
	private BalanceDetailService balanceDetailService;
	@Autowired
	private CommonConstants commonConstants;
    @Autowired
    private Environment env;
	@Autowired
	private SocketMessageService socketMessageService; 
	
	
	@Override
	public int insert(CommissionHandleTask record) {
		return commissionHandleTaskMapper.insert(record);
	}

	@Override
	public CommissionHandleTask selectByPrimaryKey(Long commissionId) {
		return commissionHandleTaskMapper.selectByPrimaryKey(commissionId);
	}

	@Override
	public int updateByPrimaryKeySelective(CommissionHandleTask record) {
		return commissionHandleTaskMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<CommissionHandleTask> selectTaskByStatus(Integer status) {
		return commissionHandleTaskMapper.selectTaskByStatus(status);
	}
	
	/**
	 * 群主抢的免死包,给发包上级奖励佣金
	 */
	private void handleFreePacketCommission(CommissionHandleTask handleData) {
		logger.info("开始分佣处理");
		UserInfo groupMasterUser = userService.selectByPrimaryKey(handleData.getGroupMasterUId());
		UserInfo sendUser = userService.selectByPrimaryKey(handleData.getUserId());
		distributeCommission(sendUser.getUpUserId(), 1,handleData);
		
		// 系统还有25%
		String distributeProportion = commonConstants.getDictionarysBy(EnvConstants.PACKET_CONF, EnvConstants.SYS_COMMISSION_PERCENT);
		BigDecimal amount = handleData.getSplitAmount().multiply(new BigDecimal(distributeProportion));
		amount = BigDecimalUtil.rounding(amount);
		
		UserWallet groupMasterWallet = userWalletService.selectByPrimaryUserId(groupMasterUser.getId());
		groupMasterWallet.setAvailableAmount(groupMasterWallet.getAvailableAmount().subtract(amount));
		Long sysUserId = Long.parseLong(env.getProperty("sys.user.id"));
		
		// 群主记录交易明细:佣金支付
		BalanceDetail balanceDetail = new BalanceDetail();
		balanceDetail.setAmount(amount.negate());
		balanceDetail.setAvailableAmount(groupMasterWallet.getAvailableAmount());
		balanceDetail.setuId(groupMasterWallet.getuId());
		balanceDetail.setTransactionType(TransactionTypeDesc.payment.toString());
		balanceDetail.setTransactionSubType(TransactionSubTypeDesc.payCommission.toString());
		balanceDetail.setRemarks(TransactionSubTypeDesc.payCommission.getDes());
		balanceDetail.setTransactionId(groupMasterWallet.getId());
		logger.info("保存佣金交易信息"+balanceDetail);
		balanceDetailService.insertSelective(balanceDetail);
		
		// 超级管理员获得25%的佣金
		UserCommission userCommission = new UserCommission();
		userCommission.setCommisionAmt(amount);
		userCommission.setDownUserId(groupMasterUser.getId());
		userCommission.setDownLevel("0");
		userCommission.setOwnUser(sysUserId);
		userCommission.setGainProportion(distributeProportion);
		userCommission.setGainDate(DateUtils.getStringDateShort());
		userCommission.setGainTime(DateUtils.getTimeShort());
		logger.info("保存佣金信息"+userCommission);
		commissionService.insertSelective(userCommission);
		// 更新超级管理金额
		UserWallet userWallet = new UserWallet();
		userWallet.setuId(sysUserId);
		userWallet.setAvailableAmount(amount);
		userWalletService.updateWalletAmount(userWallet);

		UserWallet sysWallet = userWalletService.selectByPrimaryUserId(sysUserId);
		BalanceDetail balanceUpperDetail = new BalanceDetail();
		balanceUpperDetail.setAmount(amount);
		balanceUpperDetail.setAvailableAmount(sysWallet.getAvailableAmount());
		balanceUpperDetail.setuId(sysUserId);
		balanceUpperDetail.setTransactionType(TransactionTypeDesc.income.toString());
		balanceUpperDetail.setTransactionSubType(TransactionSubTypeDesc.commissionIn.toString());
		balanceUpperDetail.setRemarks(TransactionSubTypeDesc.commissionIn.getDes());
		balanceUpperDetail.setTransactionId(userCommission.getId());
		logger.info("超管收入明细："+balanceUpperDetail);
		balanceDetailService.insertSelective(balanceUpperDetail);

		
		UserInfo sysMasterUser = userService.selectByPrimaryKey(sysUserId);
		CommissionHandleTask taskData = new CommissionHandleTask();
		taskData.setSplitAmount(amount);
		taskData.setGameId(handleData.getGameId());
		taskData.setGroupMasterUId(sysMasterUser.getId());
		taskData.setUserId(handleData.getGroupMasterUId());
		
		distributeCommission(groupMasterUser.getUpUserId(),1,taskData);
		
	}

	private void distributeCommission(Long upUser,int upLevel,CommissionHandleTask handleData){
		if(upLevel>5){
			logger.info("最多5级分佣");
			return;
		}
		
		logger.info("分佣级别[{}],扣佣用户:{}",upLevel,handleData.getGroupMasterUId());
		UserInfo user = userService.selectByPrimaryKey(upUser);
		UserWallet groupMasterWallet = userWalletService.selectByPrimaryUserId(handleData.getGroupMasterUId());
		
		String value = commonConstants.getGameRuleCommissionBy(handleData.getGameId(), RuleTypeDesc.rebate, upLevel+"");
		BigDecimal distributeProportion=new BigDecimal(value);
		BigDecimal distributeCommission = handleData.getSplitAmount().multiply(distributeProportion);
		distributeCommission = BigDecimalUtil.rounding(distributeCommission);
		
		logger.info("检查钱包是否为空:"+groupMasterWallet);
		groupMasterWallet.setAvailableAmount(groupMasterWallet.getAvailableAmount().subtract(distributeCommission));
		
		// 群主/超管记录交易明细:佣金支付
		BalanceDetail balanceDetail = new BalanceDetail();
		balanceDetail.setAmount(distributeCommission.negate());
		balanceDetail.setAvailableAmount(groupMasterWallet.getAvailableAmount());
		balanceDetail.setuId(groupMasterWallet.getuId());
		balanceDetail.setTransactionType(TransactionTypeDesc.payment.toString());
		balanceDetail.setTransactionSubType(TransactionSubTypeDesc.payCommission.toString());
		balanceDetail.setRemarks(TransactionSubTypeDesc.payCommission.getDes());
		balanceDetail.setTransactionId(groupMasterWallet.getId());
		logger.info("保存交易信息");
		balanceDetailService.insertSelective(balanceDetail);
		
		// 群主减少钱包金额
		UserWallet wallet = new UserWallet();
		wallet.setuId(groupMasterWallet.getuId());
		wallet.setAvailableAmount(distributeCommission.negate());
		userWalletService.updateWalletAmount(wallet);
		
		// 发包上级按比例获得佣金,添加佣金记录
		UserCommission userCommission = new UserCommission();
		userCommission.setCommisionAmt(distributeCommission);
		userCommission.setDownUserId(handleData.getUserId());
		userCommission.setDownLevel(upLevel+"");
		userCommission.setOwnUser(user.getId());
		userCommission.setGainProportion(distributeProportion.toEngineeringString());
		userCommission.setGainDate(DateUtils.getStringDateShort());
		userCommission.setGainTime(DateUtils.getTimeShort());
		commissionService.insertSelective(userCommission);
		// 更新发包上级金额
		UserWallet userUpperWallet = userWalletService.selectByPrimaryUserId(user.getId());
		BalanceDetail balanceUpperDetail = new BalanceDetail();
		balanceUpperDetail.setAmount(distributeCommission);
		balanceUpperDetail.setAvailableAmount(userUpperWallet.getAvailableAmount());
		balanceUpperDetail.setuId(user.getId());
		balanceUpperDetail.setTransactionType(TransactionTypeDesc.income.toString());
		balanceUpperDetail.setTransactionSubType(TransactionSubTypeDesc.commissionIn.toString());
		balanceUpperDetail.setRemarks(TransactionSubTypeDesc.commissionIn.getDes());
		balanceUpperDetail.setTransactionId(userCommission.getId());
		logger.info("上级金额收入明细："+balanceUpperDetail);
		balanceDetailService.insertSelective(balanceUpperDetail);

		UserWallet userWallet = new UserWallet();
		userWallet.setuId(user.getId());
		userWallet.setAvailableAmount(distributeCommission);
		userWalletService.updateWalletAmount(userWallet);
		
		logger.info("添加更新余额的消息通知用户:{}",user.getId());
		socketMessageService.walletRefreshNotice(null, user.getId(), "系统通知");
		
		logger.info("是否还有上级:{}",user.getUpUserId()!=null);
		if(user.getUpUserId()!=null){
			distributeCommission(user.getUpUserId(), ++upLevel,handleData);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void handleSplitCommission(CommissionHandleTask handleData) {
		// 如果有更新行数,就开始处理分佣,如果没有,这跳过不处理
		handleData.setStatus(1);
		handleData.setOldStatus(0);
		logger.info("开始处理分佣任务:{}",handleData);
		int count = commissionHandleTaskMapper.updateByPrimaryKeySelective(handleData);
		logger.info("分佣任务是否被处理:{},分佣编号:{}",count,handleData.getCommissionId());
		if(count==0){
			logger.info("分佣数据已处理或处理中:{},更新状态:{}",handleData.getCommissionId(),count);
			return;
		}
		
		logger.info("把群主抢的红包冻结金额更新到可用余额中");
		UserWallet wallet = new UserWallet();
		wallet.setuId(handleData.getGroupMasterUId());
		wallet.setFreezeAmount(handleData.getSplitAmount().negate());
		wallet.setAvailableAmount(handleData.getSplitAmount());
		
		userWalletService.updateWalletAmount(wallet);
		
		handleFreePacketCommission(handleData);
		
		// 处理完成
		handleData.setStatus(2);
		handleData.setOldStatus(1);
		commissionHandleTaskMapper.updateByPrimaryKeySelective(handleData);
		logger.info("分佣任务处理完成:{}",handleData);
		
		logger.info("添加更新余额的消息通知");
		socketMessageService.walletRefreshNotice(null, handleData.getGroupMasterUId(), "系统通知");
		
	}
 
}