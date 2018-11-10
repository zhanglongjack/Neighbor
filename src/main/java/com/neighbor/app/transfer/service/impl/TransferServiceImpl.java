package com.neighbor.app.transfer.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		Transfer transfer = new Transfer();
		BeanUtils.copyProperties(req,transfer);
		transfer.setuId(userInfo.getId());
		transfer.setAmount(amount);
		transfer.setAvailableAmount(user.getAvailableAmount());
		transfer.setCreateTime(date);
		transfer.setBeginTime(DateUtils.formatDateStr(date, DateFormateType.LANG_FORMAT));
		transfer.setOrderNo(OrderUtils.getOrderNo(OrderUtils.TRANSFER));
		transfer.setTransferWay(TransferWayDesc.out.getValue()+"");
		transfer.setStates(TransferStatusDesc.success.getValue()+"");
		transferMapper.insertSelective(transfer);

		Transfer transferIn = new Transfer();
		BeanUtils.copyProperties(transfer,transferIn);
		transferIn.setuId(transferUser.getuId());
		transferIn.setAvailableAmount(transferUser.getAvailableAmount());
		transferIn.setTransferWay(TransferWayDesc.in.getValue()+"");
		transferIn.setOrderNo(OrderUtils.getOrderNo(OrderUtils.TRANSFER));
        transferIn.setTransferUserId(userInfo.getId());
		transferMapper.insertSelective(transferIn);

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
		PageResp pageResp = new PageResp();
		pageResp.setTotalNum(total);
		pageResp.setPageList(pageList);
		pageResp.setPageIndex(transfer.getPageTools().getIndex().longValue());
		pageResp.setPageSize(transfer.getPageTools().getPageSize().longValue());
		result.addBody("resp", pageResp);
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
