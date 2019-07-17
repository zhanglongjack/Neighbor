package com.neighbor.app.transfer.service.impl;

import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.balance.entity.BalanceDetail;
import com.neighbor.app.balance.po.TransactionItemDesc;
import com.neighbor.app.balance.po.TransactionSubTypeDesc;
import com.neighbor.app.balance.po.TransactionTypeDesc;
import com.neighbor.app.balance.service.BalanceDetailService;
import com.neighbor.app.common.util.OrderUtils;
import com.neighbor.app.transfer.constants.TransferStatusDesc;
import com.neighbor.app.transfer.constants.TransferWayDesc;
import com.neighbor.app.transfer.dao.TransferMapper;
import com.neighbor.app.transfer.entity.Transfer;
import com.neighbor.app.transfer.po.TransferReq;
import com.neighbor.app.transfer.po.TransferResp;
import com.neighbor.app.transfer.service.TransferService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.common.util.*;
import com.neighbor.common.websoket.service.SocketMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {
	private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);


	@Autowired
	private TransferMapper transferMapper;

    @Autowired
	private UserWalletService userWalletService;

    @Autowired
    private BalanceDetailService balanceDetailService;

	@Autowired
	private SocketMessageService socketMessageService;



	/**
     * 内部转账方法
     * @param req
     * @return
     * @throws Exception
     */
	@Override
	@Transactional(readOnly = false,rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
	public ResponseResult transfer(UserInfo userInfo , TransferReq req) throws Exception {
		ResponseResult responseResult = transferForChat(userInfo,req);
		return responseResult;
	}

    @Override
    public ResponseResult transferRecord(UserInfo user, Transfer transfer) throws Exception {
		ResponseResult result = new ResponseResult();
		transfer.setuId(user.getId());
		Long total = transferMapper.selectPageTotalCount(transfer);
		List<Transfer> pageList = transferMapper.selectPageByObjectForList(transfer);
		PageTools pageTools = transfer.getPageTools();
		pageTools.setTotal(total);
		result.addBody("resultList", pageList);
		result.addBody("pageTools", pageTools);
        return result;
    }

	@Override
	public ResponseResult transferInfo(Transfer transfer) throws Exception {
		ResponseResult responseResult = new ResponseResult();
        transfer = transferMapper.selectByPrimaryKey(transfer.getId());
        responseResult.addBody("transfer",transfer);
		return responseResult;
	}

    @Override
	@Transactional(readOnly = false,rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public ResponseResult transferForChat(UserInfo userInfo, TransferReq req) throws Exception {
		ResponseResult responseResult = new ResponseResult();
		UserWallet userWallet = userWalletService.selectByPrimaryUserId(userInfo.getId());
		BigDecimal amount = new BigDecimal(req.getAmount());
		if(userWallet==null){
			responseResult.setErrorCode(1);//失败
			responseResult.setErrorMessage("用户不存在！");
			return responseResult;
		}
		UserWallet transferUser = userWalletService.selectByPrimaryUserId(req.getTransferUserId());
		if(transferUser==null){
			responseResult.setErrorCode(1);//失败
			responseResult.setErrorMessage("转账用户不存在！");
			return responseResult;
		}
		if(userWallet.getAvailableAmount().compareTo(amount)<0){
			responseResult.setErrorCode(1);//失败
			responseResult.setErrorMessage("可用余额不足！当前余额："+userWallet.getAvailableAmount());
			return responseResult;
		}
		UserWallet updateWallet = new UserWallet();
		updateWallet.setAvailableAmount(amount.negate());
		updateWallet.setuId(userInfo.getId());
		userWalletService.updateWalletAmount(updateWallet);
		userWallet = userWalletService.selectByPrimaryUserId(userInfo.getId());


		Date date = new Date();
		//转出交易
		Transfer transfer = new Transfer();
		BeanUtils.copyProperties(req,transfer);
		transfer.setuId(userInfo.getId());
		transfer.setAmount(amount);
		transfer.setAvailableAmount(userWallet.getAvailableAmount());
		transfer.setCreateTime(date);
		transfer.setBeginTime(DateUtils.formatDateStr(date, DateFormateType.LANG_FORMAT));
		transfer.setOrderNo(OrderUtils.getOrderNo(OrderUtils.TRANSFER,userInfo.getId()));
		transfer.setTransferWay(TransferWayDesc.out.toString());
		transfer.setStates(TransferStatusDesc.processing.toString());
		transferMapper.insertSelective(transfer);
		//转出交易明细
		BalanceDetail balanceDetailOut = new BalanceDetail();
		balanceDetailOut.setAmount(amount.negate());
		balanceDetailOut.setAvailableAmount(transfer.getAvailableAmount());
		balanceDetailOut.setuId(transfer.getuId());
		balanceDetailOut.setTransactionType(TransactionTypeDesc.payment.toString());
		balanceDetailOut.setTransactionSubType(TransactionSubTypeDesc.transferOut.toString());
		balanceDetailOut.setRemarks(TransactionItemDesc.transfer.getDes()+StringUtil.split_
				+TransactionSubTypeDesc.transferOut.getDes()+req.getTransferUserId());
		balanceDetailOut.setTransactionId(transfer.getId());
		balanceDetailService.insertSelective(balanceDetailOut);

		TransferResp transferResp = new TransferResp();
		transferResp.setOrderNo(transfer.getOrderNo());
		responseResult.addBody("transfer",transfer);
		responseResult.addBody("userWallet",userWallet);

        return responseResult;
    }

	@Override
	@Transactional(readOnly = false,rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
	public ResponseResult transferReceive(UserInfo user, TransferReq req) throws Exception {
		ResponseResult responseResult = new ResponseResult();
		Transfer transfer = transferMapper.selectByPrimaryKey(req.getTransferId());
		if(transfer==null||!TransferStatusDesc.processing.toString().equals(transfer.getStates())||!transfer.getTransferUserId().equals(user.getId())){
			responseResult.setErrorCode(1);//失败
			responseResult.setErrorMessage("请求有误，暂无法受理！");
			return responseResult;
		}
		UserWallet transferUser = userWalletService.selectByPrimaryUserId(transfer.getTransferUserId());
		if(transferUser==null){
			responseResult.setErrorCode(1);//失败
			responseResult.setErrorMessage("转账用户不存在！");
			return responseResult;
		}
		BigDecimal amount = transfer.getAmount();
		UserWallet updateTransferUserWallet = new UserWallet();
		updateTransferUserWallet.setAvailableAmount(amount);
		updateTransferUserWallet.setuId(transfer.getTransferUserId());
		userWalletService.updateWalletAmount(updateTransferUserWallet);
		transferUser = userWalletService.selectByPrimaryUserId(transfer.getTransferUserId());

		Date date = new Date();

		//转入交易
		Transfer transferIn = new Transfer();
		BeanUtils.copyProperties(transfer,transferIn);
		transferIn.setId(null);
		transferIn.setCreateTime(date);
		transferIn.setUpdateTime(date);
		transferIn.setuId(transferUser.getuId());
		transferIn.setAvailableAmount(transferUser.getAvailableAmount());
		transferIn.setTransferWay(TransferWayDesc.in.toString());
		transferIn.setOrderNo(OrderUtils.getOrderNo(OrderUtils.TRANSFER,transferUser.getuId()));
		transferIn.setTransferUserId(transfer.getuId());
		transferIn.setStates(TransferStatusDesc.success.toString());
		transferMapper.insertSelective(transferIn);

		//转入交易明细
		BalanceDetail balanceDetailIn = new BalanceDetail();
		balanceDetailIn.setAmount(amount);
		balanceDetailIn.setAvailableAmount(transferUser.getAvailableAmount());
		balanceDetailIn.setuId(transferUser.getuId());
		balanceDetailIn.setTransactionType(TransactionTypeDesc.receipt.toString());
		balanceDetailIn.setTransactionSubType(TransactionSubTypeDesc.transferIn.toString());
		balanceDetailIn.setRemarks(TransactionItemDesc.transfer.getDes()+StringUtil.split_
				+transfer.getuId()+TransactionSubTypeDesc.transferIn.getDes());
		balanceDetailIn.setTransactionId(transferIn.getId());
		balanceDetailService.insertSelective(balanceDetailIn);

		//修改转账状态
		transfer.setStates(TransferStatusDesc.success.toString());
		transfer.setUpdateTime(date);
		transferMapper.updateByPrimaryKeySelective(transfer);
		responseResult.addBody("transfer",transfer);
		responseResult.addBody("userWallet",transferUser);
		return responseResult;
	}

	@Override
	public ResponseResult transferBackJob() throws Exception {
		Transfer transfer = new Transfer();
		transfer.setStates(TransferStatusDesc.processing.toString());
		transfer.setTransferBack("1");
		PageTools pageTools = new PageTools();
		pageTools.setPageSize(Integer.MAX_VALUE);
		transfer.setPageTools(pageTools);
		List<Transfer> transferList = transferMapper.selectPageByObjectForList(transfer);
		if(transferList!=null&&transferList.size()>0){
			for(Transfer t : transferList){
				try {
					transferBack(t);
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
			}
		}
		return new ResponseResult();
	}

	@Override
	@Transactional(readOnly = false,rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
	public ResponseResult transferBack(Transfer transfer) throws Exception {
		ResponseResult responseResult = new ResponseResult();
		BigDecimal amount = transfer.getAmount();
		UserWallet updateTransferUserWallet = new UserWallet();
		updateTransferUserWallet.setAvailableAmount(amount);
		updateTransferUserWallet.setuId(transfer.getuId());
		userWalletService.updateWalletAmount(updateTransferUserWallet);
		UserWallet transferUser = userWalletService.selectByPrimaryUserId(transfer.getuId());

		Date date = new Date();

		//转入交易明细
		BalanceDetail balanceDetailIn = new BalanceDetail();
		balanceDetailIn.setAmount(amount);
		balanceDetailIn.setAvailableAmount(transferUser.getAvailableAmount());
		balanceDetailIn.setuId(transferUser.getuId());
		balanceDetailIn.setTransactionType(TransactionTypeDesc.receipt.toString());
		balanceDetailIn.setTransactionSubType(TransactionSubTypeDesc.transferIn.toString());
		balanceDetailIn.setRemarks(TransactionSubTypeDesc.transferBack.getDes()+transfer.getuId());
		balanceDetailIn.setTransactionId(transfer.getId());
		balanceDetailService.insertSelective(balanceDetailIn);

		//修改转账状态
		Transfer updateTransfer = new Transfer();
		updateTransfer.setStates(TransferStatusDesc.back.toString());
		updateTransfer.setUpdateTime(date);
		updateTransfer.setId(transfer.getId());
		transferMapper.updateByPrimaryKeySelective(updateTransfer);

		socketMessageService.walletRefreshNotice(null, transfer.getuId(), "系统通知");

		return responseResult;
	}
}
