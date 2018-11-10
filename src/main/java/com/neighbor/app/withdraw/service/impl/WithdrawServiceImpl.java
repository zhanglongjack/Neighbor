package com.neighbor.app.withdraw.service.impl;

import com.neighbor.app.common.util.OrderUtils;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.app.withdraw.dao.WithdrawMapper;
import com.neighbor.app.withdraw.entity.Withdraw;
import com.neighbor.app.withdraw.po.WithdrawStatusDesc;
import com.neighbor.app.withdraw.service.WithdrawService;
import com.neighbor.common.util.PageResp;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Service
public class WithdrawServiceImpl implements WithdrawService {
    private static final Logger logger = LoggerFactory.getLogger(WithdrawServiceImpl.class);

    @Autowired
    private WithdrawMapper withdrawMapper;
    @Autowired
    private UserWalletService userWalletService;

    @Override
    @Transactional(readOnly = false,rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public ResponseResult withdraw(UserInfo userInfo, Withdraw withdraw) throws Exception {
        ResponseResult responseResult = new ResponseResult();
        UserWallet user = userWalletService.lockUserWalletByUserId(userInfo.getId());
        if(user==null){
            responseResult.setErrorCode(1);//失败
            responseResult.setErrorMessage("用户不存在！");
            return responseResult;
        }
        BigDecimal amount = withdraw.getAmount();
        if(user.getAvailableAmount().compareTo(amount)<0){
            responseResult.setErrorCode(1);//失败
            responseResult.setErrorMessage("可用余额不足！当前余额："+user.getAvailableAmount());
            return responseResult;
        }
        user.setAvailableAmount(user.getAvailableAmount().subtract(amount));
        Date date = new Date();
        withdraw.setId(null);
        withdraw.setuId(userInfo.getId());
        withdraw.setOrderNo(OrderUtils.getOrderNo(OrderUtils.WITHDRAW));
        withdraw.setCreateTime(date);
        withdraw.setAvailableAmount(user.getAvailableAmount());
        withdraw.setStates(WithdrawStatusDesc.initial.getValue()+"");
        user.setUpdateTime(date);
        userWalletService.updateByPrimaryKeySelective(user);
        withdrawMapper.insertSelective(withdraw);
        return responseResult;
    }

    @Override
    public ResponseResult withdrawRecord(UserInfo user, Withdraw withdraw) throws Exception {
        ResponseResult result = new ResponseResult();
        withdraw.setuId(user.getId());
        Long total = withdrawMapper.selectPageTotalCount(withdraw);
        List<Withdraw> pageList = withdrawMapper.selectPageByObjectForList(withdraw);
        PageResp pageResp = new PageResp();
        pageResp.setTotalNum(total);
        pageResp.setPageList(pageList);
        pageResp.setPageIndex(withdraw.getPageTools().getIndex().longValue());
        pageResp.setPageSize(withdraw.getPageTools().getPageSize().longValue());
        result.addBody("resp", pageResp);
        return result;
    }

    @Override
    public ResponseResult withdrawInfo(Withdraw withdraw) throws Exception {
        ResponseResult responseResult = new ResponseResult();
        withdraw = withdrawMapper.selectByPrimaryKey(withdraw.getId());
        responseResult.addBody("withdraw",withdraw);
        return responseResult;
    }

}