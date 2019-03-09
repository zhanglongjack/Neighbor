package com.neighbor.app.recharge.service.impl;

import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.balance.entity.BalanceDetail;
import com.neighbor.app.balance.po.TransactionItemDesc;
import com.neighbor.app.balance.po.TransactionSubTypeDesc;
import com.neighbor.app.balance.po.TransactionTypeDesc;
import com.neighbor.app.balance.service.BalanceDetailService;
import com.neighbor.app.common.util.OrderUtils;
import com.neighbor.app.recharge.constants.ChannelTypeDesc;
import com.neighbor.app.recharge.constants.RechargeStatusDesc;
import com.neighbor.app.recharge.controller.RechargeController;
import com.neighbor.app.recharge.dao.RechargeMapper;
import com.neighbor.app.recharge.entity.Recharge;
import com.neighbor.app.recharge.po.RechargeRecord;
import com.neighbor.app.recharge.service.RechargeService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.common.exception.ParamsCheckException;
import com.neighbor.common.sms.TencentSms;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
        //判断验证码
        boolean isValid = recharge.getVerfiyCode().equals(TencentSms.smsCache.get(recharge.getPhone()));
        if (!isValid) {
            logger.info("验证码错误");
            throw new ParamsCheckException(ErrorCodeDesc.failed.getValue(), "验证码错误");
        }
        Date date = new Date();
        UserWallet userWallet = userWalletService.selectByPrimaryUserId(user.getId());
        recharge.setId(null);
        recharge.setCreateTime(date);
        recharge.setOrderNo(OrderUtils.getOrderNo(OrderUtils.RECHARGE));
        recharge.setuId(user.getId());
        recharge.setAvailableAmount(userWallet.getAvailableAmount());
        if(ChannelTypeDesc.offline.toString().equals(recharge.getChannelType())){
            recharge.setStates(RechargeStatusDesc.initial.toString());
            rechargeMapper.insertSelective(recharge);
            TencentSms.smsCache.remove(recharge.getPhone());
            return  responseResult;
        }else{
            recharge.setStates(RechargeStatusDesc.success.toString());
            rechargeMapper.insertSelective(recharge);
        }

        UserWallet updateWallet = new UserWallet();
        //退回金额
        updateWallet.setAvailableAmount(recharge.getAmount());
        updateWallet.setuId(user.getId());
        userWalletService.updateWalletAmount(updateWallet);
        userWallet = userWalletService.selectByPrimaryUserId(user.getId());

        //充值交易明细
        BalanceDetail balanceDetail = new BalanceDetail();
        balanceDetail.setAmount(recharge.getAmount());
        balanceDetail.setAvailableAmount(recharge.getAvailableAmount());
        balanceDetail.setuId(recharge.getuId());
        balanceDetail.setTransactionType(TransactionTypeDesc.receipt.toString());
        balanceDetail.setTransactionSubType(TransactionSubTypeDesc.recharge.toString());
        if(recharge.getRemarks()!=null){
            balanceDetail.setRemarks(TransactionItemDesc.recharge.getDes()+ StringUtil.split_
                    +recharge.getRemarks());
        }else{
            balanceDetail.setRemarks(TransactionItemDesc.recharge.getDes());
        }

        balanceDetail.setTransactionId(recharge.getId());
        balanceDetailService.insertSelective(balanceDetail);


        TencentSms.smsCache.remove(recharge.getPhone());
        responseResult.addBody("userWallet", userWallet);
        return responseResult;
    }

//    @Override
//    public RechargeRecordResp rechargeRecord(UserInfo user,Recharge recharge) throws Exception {
//        RechargeRecordResp rechargeRecordResp = new RechargeRecordResp();
//        recharge.setuId(user.getId());
//        Long total = rechargeMapper.selectPageTotalCount(recharge);
//        List<Recharge> recharges = rechargeMapper.selectPageByObjectForList(recharge);
//        List<RechargeRecord> pageList = new ArrayList<RechargeRecord>();
//        if(recharges!=null&&recharges.size()>0){
//            for(Recharge rec : recharges){
//                RechargeRecord rechargeRecord = new RechargeRecord();
//                BeanUtils.copyProperties(rec,rechargeRecord);
//                pageList.add(rechargeRecord);
//            }
//        }
//
//        rechargeRecordResp.setTotalNum(total);
//        rechargeRecordResp.setPageList(pageList);
//        rechargeRecordResp.setPageIndex(recharge.getPageTools().getIndex().longValue());
//        rechargeRecordResp.setPageSize(recharge.getPageTools().getPageSize().longValue());
//        return rechargeRecordResp;
//
//    }

    @Override
    public ResponseResult rechargeInfo(Recharge recharge) throws Exception {
        ResponseResult responseResult = new ResponseResult();
        Recharge rechargeInfo = rechargeMapper.selectByPrimaryKey(recharge.getId());
        RechargeRecord rechargeRecord = new RechargeRecord();
        BeanUtils.copyProperties(rechargeInfo,rechargeRecord);
        responseResult.addBody("rechargeInfo",rechargeRecord);
        return responseResult;
    }

	@Override
	public Long selectPageTotalCount(Recharge record) {
		return rechargeMapper.selectPageTotalCount(record);
	}

	@Override
	public List<Recharge> selectPageByObjectForList(Recharge record) {
		return rechargeMapper.selectPageByObjectForList(record);
	}

    @Override
    @Transactional(readOnly = false,rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public ResponseResult modifyRecharge(UserInfo user, Recharge recharge) {
        ResponseResult result = new ResponseResult();
        Date date = new Date();
        recharge.setId(recharge.getRecordId());
        recharge.setUpdateTime(date);
        rechargeMapper.updateByPrimaryKeySelective(recharge);
        if(RechargeStatusDesc.success.toString().equals(recharge.getStates())){
            //成功
            Recharge temp = rechargeMapper.selectByPrimaryKey(recharge.getId());
            UserWallet updateWallet = new UserWallet();
            updateWallet.setAvailableAmount(temp.getAmount());
            updateWallet.setuId(temp.getuId());
            userWalletService.updateWalletAmount(updateWallet);
            UserWallet userWallet = userWalletService.selectByPrimaryUserId(temp.getuId());

            //充值交易明细
            BalanceDetail balanceDetail = new BalanceDetail();
            balanceDetail.setAmount(temp.getAmount());
            balanceDetail.setAvailableAmount(userWallet.getAvailableAmount());
            balanceDetail.setuId(temp.getuId());
            balanceDetail.setTransactionType(TransactionTypeDesc.receipt.toString());
            balanceDetail.setTransactionSubType(TransactionSubTypeDesc.recharge.toString());
            if(recharge.getRemarks()!=null){
                balanceDetail.setRemarks(TransactionItemDesc.recharge.getDes()+ StringUtil.split_
                        +recharge.getRemarks());
            }else{
                balanceDetail.setRemarks(TransactionItemDesc.recharge.getDes());
            }

            balanceDetail.setTransactionId(temp.getId());
            balanceDetailService.insertSelective(balanceDetail);
            recharge.setuId(temp.getuId());
        }
        return result;
    }
}
