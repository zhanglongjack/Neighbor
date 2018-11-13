package com.neighbor.app.recharge.service.impl;

import com.neighbor.app.balance.entity.BalanceDetail;
import com.neighbor.app.balance.po.TransactionItemDesc;
import com.neighbor.app.balance.po.TransactionSubTypeDesc;
import com.neighbor.app.balance.po.TransactionTypeDesc;
import com.neighbor.app.balance.service.BalanceDetailService;
import com.neighbor.app.common.util.OrderUtils;
import com.neighbor.app.recharge.controller.RechargeController;
import com.neighbor.app.recharge.dao.RechargeMapper;
import com.neighbor.app.recharge.entity.Recharge;
import com.neighbor.app.recharge.po.RechargeStatusDesc;
import com.neighbor.app.recharge.po.RechargeRecord;
import com.neighbor.app.recharge.po.RechargeRecordResp;
import com.neighbor.app.recharge.service.RechargeService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RechargeServiceImpl implements RechargeService {
    private static final Logger logger = LoggerFactory.getLogger(RechargeController.class);
    @Autowired
    private RechargeMapper rechargeMapper;

    @Autowired
    private UserWalletService userWalletService;

    @Autowired
    private BalanceDetailService balanceDetailService;

    @Override
    @Transactional(readOnly = false,rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public ResponseResult recharge(UserInfo user, Recharge recharge) throws Exception {
        ResponseResult responseResult = new ResponseResult();
        Date date = new Date();
        UserWallet userWallet = userWalletService.lockUserWalletByUserId(user.getId());
        userWallet.setAvailableAmount(userWallet.getAvailableAmount().add(recharge.getAmount()));
        userWallet.setUpdateTime(date);
        recharge.setId(null);
        recharge.setCreateTime(date);
        recharge.setOrderNo(OrderUtils.getOrderNo(OrderUtils.RECHARGE));
        recharge.setuId(user.getId());
        recharge.setAvailableAmount(userWallet.getAvailableAmount());
        recharge.setStates(RechargeStatusDesc.success.getValue()+"");
        rechargeMapper.insertSelective(recharge);

        //充值交易明细
        BalanceDetail balanceDetail = new BalanceDetail();
        balanceDetail.setAmount(recharge.getAmount());
        balanceDetail.setAvailableAmount(recharge.getAvailableAmount());
        balanceDetail.setuId(recharge.getuId());
        balanceDetail.setTransactionType(TransactionTypeDesc.receipt.getValue());
        balanceDetail.setTransactionSubType(TransactionSubTypeDesc.recharge.getValue());
        if(recharge.getRemarks()!=null){
            balanceDetail.setRemarks(TransactionItemDesc.recharge.getDes()+ StringUtil.split_
                    +recharge.getRemarks());
        }else{
            balanceDetail.setRemarks(TransactionItemDesc.recharge.getDes());
        }

        balanceDetail.setTransactionId(recharge.getId());
        balanceDetailService.insertSelective(balanceDetail);


        userWalletService.updateByPrimaryKeySelective(userWallet);
        return responseResult;
    }

    @Override
    public RechargeRecordResp rechargeRecord(UserInfo user,Recharge recharge) throws Exception {
        RechargeRecordResp rechargeRecordResp = new RechargeRecordResp();
        recharge.setuId(user.getId());
        Long total = rechargeMapper.selectPageTotalCount(recharge);
        List<Recharge> recharges = rechargeMapper.selectPageByObjectForList(recharge);
        List<RechargeRecord> pageList = new ArrayList<RechargeRecord>();
        if(recharges!=null&&recharges.size()>0){
            for(Recharge rec : recharges){
                RechargeRecord rechargeRecord = new RechargeRecord();
                BeanUtils.copyProperties(rec,rechargeRecord);
                pageList.add(rechargeRecord);
            }
        }

        rechargeRecordResp.setTotalNum(total);
        rechargeRecordResp.setPageList(pageList);
        rechargeRecordResp.setPageIndex(recharge.getPageTools().getIndex().longValue());
        rechargeRecordResp.setPageSize(recharge.getPageTools().getPageSize().longValue());
        return rechargeRecordResp;

    }

    @Override
    public ResponseResult rechargeInfo(Recharge recharge) throws Exception {
        ResponseResult responseResult = new ResponseResult();
        Recharge rechargeInfo = rechargeMapper.selectByPrimaryKey(recharge.getId());
        RechargeRecord rechargeRecord = new RechargeRecord();
        BeanUtils.copyProperties(rechargeInfo,rechargeRecord);
        responseResult.addBody("rechargeInfo",rechargeRecord);
        return responseResult;
    }
}
