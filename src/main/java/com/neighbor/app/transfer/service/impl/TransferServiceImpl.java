package com.neighbor.app.transfer.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.neighbor.app.balance.entity.BalanceDetail;
import com.neighbor.app.balance.po.TransactionItemDesc;
import com.neighbor.app.balance.po.TransactionSubTypeDesc;
import com.neighbor.app.balance.po.TransactionTypeDesc;
import com.neighbor.app.balance.service.BalanceDetailService;
import com.neighbor.app.common.util.OrderUtils;
import com.neighbor.app.transfer.po.*;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.common.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neighbor.app.transfer.dao.TransferMapper;
import com.neighbor.app.transfer.entity.Transfer;
import com.neighbor.app.transfer.service.TransferService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferServiceImpl implements TransferService {

	@Autowired
	private TransferMapper transferMapper;

    @Autowired
	private UserWalletService userWalletService;

    @Autowired
    private BalanceDetailService balanceDetailService;



	/**
     * 内部转账方法
     * @param req
     * @return
     * @throws Exception
     */
	@Override
	@Transactional(readOnly = false,rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
	public ResponseResult transfer(UserInfo userInfo , TransferReq req) throws Exception {
		ResponseResult responseResult = new ResponseResult();
		UserWallet user = userWalletService.lockUserWalletByUserId(userInfo.getId());
		if(user==null){
			responseResult.setErrorCode(1);//失败
			responseResult.setErrorMessage("用户不存在！");
			return responseResult;
		}
		UserWallet transferUser = userWalletService.lockUserWalletByUserId(req.getTransferUserId());
		if(transferUser==null){
			responseResult.setErrorCode(1);//失败
			responseResult.setErrorMessage("转账用户不存在！");
			return responseResult;
		}
		BigDecimal amount = new BigDecimal(req.getAmount());
		if(user.getAvailableAmount().compareTo(amount)<0){
			responseResult.setErrorCode(1);//失败
			responseResult.setErrorMessage("可用余额不足！当前余额："+user.getAvailableAmount());
			return responseResult;
		}
		user.setAvailableAmount(user.getAvailableAmount().subtract(amount));
		transferUser.setAvailableAmount(transferUser.getAvailableAmount().add(amount));
		userWalletService.updateByPrimaryKeySelective(user);
		userWalletService.updateByPrimaryKeySelective(transferUser);
		Date date = new Date();
		//转出交易
		Transfer transfer = new Transfer();
		BeanUtils.copyProperties(req,transfer);
		transfer.setuId(userInfo.getId());
		transfer.setAmount(amount);
		transfer.setAvailableAmount(user.getAvailableAmount());
		transfer.setCreateTime(date);
		transfer.setBeginTime(DateUtils.formatDateStr(date, DateFormateType.LANG_FORMAT));
		transfer.setOrderNo(OrderUtils.getOrderNo(OrderUtils.TRANSFER));
		transfer.setTransferWay(TransferWayDesc.out.toString());
		transfer.setStates(TransferStatusDesc.success.toString());
		transferMapper.insertSelective(transfer);
		//转出交易明细
		BalanceDetail balanceDetailOut = new BalanceDetail();
		balanceDetailOut.setAmount(amount);
		balanceDetailOut.setAvailableAmount(transfer.getAvailableAmount());
		balanceDetailOut.setuId(transfer.getuId());
		balanceDetailOut.setTransactionType(TransactionTypeDesc.payment.toString());
		balanceDetailOut.setTransactionSubType(TransactionSubTypeDesc.transferOut.toString());
		balanceDetailOut.setRemarks(TransactionItemDesc.transfer.getDes()+StringUtil.split_
				+TransactionSubTypeDesc.transferOut.getDes()+transferUser.getuId());
		balanceDetailOut.setTransactionId(transfer.getId());
		balanceDetailService.insertSelective(balanceDetailOut);


		//转入交易
		Transfer transferIn = new Transfer();
		BeanUtils.copyProperties(transfer,transferIn);
		transferIn.setId(null);
		transferIn.setuId(transferUser.getuId());
		transferIn.setAvailableAmount(transferUser.getAvailableAmount());
		transferIn.setTransferWay(TransferWayDesc.in.toString());
		transferIn.setOrderNo(OrderUtils.getOrderNo(OrderUtils.TRANSFER));
        transferIn.setTransferUserId(userInfo.getId());
		transferMapper.insertSelective(transferIn);

		//转入交易明细
		BalanceDetail balanceDetailIn = new BalanceDetail();
		balanceDetailIn.setAmount(amount);
		balanceDetailIn.setAvailableAmount(transferUser.getAvailableAmount());
		balanceDetailIn.setuId(transferUser.getuId());
		balanceDetailIn.setTransactionType(TransactionTypeDesc.receipt.toString());
		balanceDetailIn.setTransactionSubType(TransactionSubTypeDesc.transferIn.toString());
		balanceDetailIn.setRemarks(TransactionItemDesc.transfer.getDes()+StringUtil.split_
				+TransactionSubTypeDesc.transferIn.getDes()+transfer.getuId());
		balanceDetailIn.setTransactionId(transferIn.getId());
		balanceDetailService.insertSelective(balanceDetailIn);
		TransferResp transferResp = new TransferResp();
		transferResp.setOrderNo(transfer.getOrderNo());
		responseResult.addBody("resp",transferResp);
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
}
