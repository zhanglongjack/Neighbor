package com.neighbor.app.withdraw.service.impl;

import com.neighbor.app.balance.entity.BalanceDetail;
import com.neighbor.app.balance.po.TransactionItemDesc;
import com.neighbor.app.balance.po.TransactionSubTypeDesc;
import com.neighbor.app.balance.po.TransactionTypeDesc;
import com.neighbor.app.balance.service.BalanceDetailService;
import com.neighbor.app.common.util.OrderUtils;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.app.withdraw.dao.WithdrawMapper;
import com.neighbor.app.withdraw.entity.Withdraw;
import com.neighbor.app.withdraw.po.WithdrawStatusDesc;
import com.neighbor.app.withdraw.service.WithdrawService;
import com.neighbor.common.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Service
public class WithdrawServiceImpl implements WithdrawService {
    private static final Logger logger = LoggerFactory.getLogger(WithdrawServiceImpl.class);

    @Autowired
    private WithdrawMapper withdrawMapper;
    @Autowired
    private UserWalletService userWalletService;

    @Autowired
    private Environment env;

    @Autowired
    private BalanceDetailService balanceDetailService;

    @Override
    @Transactional(readOnly = false,rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public ResponseResult withdraw(UserInfo userInfo, Withdraw withdraw) throws Exception {
        ResponseResult responseResult = new ResponseResult();
        UserWallet userWallet = userWalletService.lockUserWalletByUserId(userInfo.getId());
        if(userWallet==null){
            responseResult.setErrorCode(1);//失败
            responseResult.setErrorMessage("用户不存在！");
            return responseResult;
        }
        BigDecimal amount = BigDecimalUtil.rounding(withdraw.getAmount());
        BigDecimal withdrawRate = new BigDecimal(env.getProperty("withdraw_rate"));
        BigDecimal cost = BigDecimalUtil.rounding(amount.multiply(withdrawRate));
        BigDecimal payAmount = BigDecimalUtil.rounding(amount.add(cost));
        if(userWallet.getAvailableAmount().compareTo(payAmount)<0){
            responseResult.setErrorCode(1);//失败
            responseResult.setErrorMessage("可用余额不足："+payAmount+"！当前余额："+userWallet.getAvailableAmount());
            return responseResult;
        }
        userWallet.setAvailableAmount(userWallet.getAvailableAmount().subtract(payAmount));
        Date date = new Date();
        withdraw.setId(null);
        withdraw.setuId(userInfo.getId());
        withdraw.setOrderNo(OrderUtils.getOrderNo(OrderUtils.WITHDRAW));
        withdraw.setCreateTime(date);
        withdraw.setAvailableAmount(userWallet.getAvailableAmount());
        withdraw.setStates(WithdrawStatusDesc.initial.toString());
        userWallet.setUpdateTime(date);


        BigDecimal actualAmount = amount;
        withdraw.setActualAmount(actualAmount);
        withdraw.setCost(cost.negate());
        withdraw.setAmount(amount.negate());
        userWalletService.updateByPrimaryKeySelective(userWallet);
        withdrawMapper.insertSelective(withdraw);

        //提现交易明细
        BalanceDetail balanceDetail = new BalanceDetail();
        balanceDetail.setAmount(withdraw.getAmount());
        balanceDetail.setAvailableAmount(withdraw.getAvailableAmount().add(cost));
        balanceDetail.setuId(withdraw.getuId());
        balanceDetail.setTransactionType(TransactionTypeDesc.expenditure.toString());
        balanceDetail.setTransactionSubType(TransactionSubTypeDesc.withdraw.toString());
        if(withdraw.getRemarks()!=null){
            balanceDetail.setRemarks(TransactionItemDesc.withdraw.getDes()+ StringUtil.split_
                    +withdraw.getRemarks());
        }else{
            balanceDetail.setRemarks(TransactionItemDesc.withdraw.getDes());
        }

        balanceDetail.setTransactionId(withdraw.getId());
        balanceDetailService.insertSelective(balanceDetail);

        //提现手续费交易明细
        BalanceDetail balanceDetailCost = new BalanceDetail();
        balanceDetailCost.setAmount(cost.negate());
        balanceDetailCost.setAvailableAmount(withdraw.getAvailableAmount());
        balanceDetailCost.setuId(withdraw.getuId());
        balanceDetailCost.setTransactionType(TransactionTypeDesc.expenditure.toString());
        balanceDetailCost.setTransactionSubType(TransactionSubTypeDesc.withdrawCost.toString());
        balanceDetailCost.setRemarks(TransactionSubTypeDesc.withdrawCost.getDes());
        balanceDetailCost.setTransactionId(withdraw.getId());
        balanceDetailService.insertSelective(balanceDetailCost);

        responseResult.addBody("userWallet", userWallet);
        return responseResult;
    }

    @Override
    public ResponseResult withdrawRecord(UserInfo user, Withdraw withdraw) throws Exception {
        ResponseResult result = new ResponseResult();
        withdraw.setuId(user.getId());
        Long total = withdrawMapper.selectPageTotalCount(withdraw);
        List<Withdraw> pageList = withdrawMapper.selectPageByObjectForList(withdraw);
        /*PageResp pageResp = new PageResp();
        pageResp.setTotalNum(total);
        pageResp.setPageList(pageList);
        pageResp.setPageIndex(withdraw.getPageTools().getIndex().longValue());
        pageResp.setPageSize(withdraw.getPageTools().getPageSize().longValue());*/
        PageTools pageTools = withdraw.getPageTools();
        pageTools.setTotal(total);
        result.addBody("resultList", pageList);
        result.addBody("pageTools", pageTools);
        return result;
    }

    @Override
    public ResponseResult withdrawInfo(Withdraw withdraw) throws Exception {
        ResponseResult responseResult = new ResponseResult();
        withdraw = withdrawMapper.selectByPrimaryKey(withdraw.getId());
        responseResult.addBody("withdraw",withdraw);
        return responseResult;
    }

    @Override
    public ResponseResult preWithdraw(UserInfo userInfo, Withdraw withdraw) throws Exception {
        ResponseResult responseResult = new ResponseResult();
        UserWallet userWallet = userWalletService.lockUserWalletByUserId(userInfo.getId());
        if(userWallet==null){
            responseResult.setErrorCode(1);//失败
            responseResult.setErrorMessage("用户不存在！");
            logger.info("用户信息不存在！");
            return responseResult;
        }
        HashMap<String,Object> body = new HashMap<String,Object>();
        body.put("availableAmount",userWallet.getAvailableAmount());
        body.put("withdrawRate",env.getProperty("withdraw_rate"));
        responseResult.addBody("resp",body);
        return responseResult;
    }


}
