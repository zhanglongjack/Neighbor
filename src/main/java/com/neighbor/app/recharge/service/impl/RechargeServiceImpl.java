package com.neighbor.app.recharge.service.impl;

import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.balance.entity.BalanceDetail;
import com.neighbor.app.balance.po.TransactionItemDesc;
import com.neighbor.app.balance.po.TransactionSubTypeDesc;
import com.neighbor.app.balance.po.TransactionTypeDesc;
import com.neighbor.app.balance.service.BalanceDetailService;
import com.neighbor.app.common.util.OrderUtils;
import com.neighbor.app.pay.common.PayUtils;
import com.neighbor.app.pay.constants.MethodDesc;
import com.neighbor.app.pay.entity.NotifyResp;
import com.neighbor.app.pay.entity.PayResp;
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
import com.neighbor.common.constants.CommonConstants;
import com.neighbor.common.constants.EnvConstants;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URLDecoder;
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

    @Autowired
    private PayUtils payUtils;

    @Autowired
    private CommonConstants commonConstants;

    @Override
    @Transactional(readOnly = false,rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public ResponseResult recharge(UserInfo user, Recharge recharge) throws Exception {
        ResponseResult responseResult = new ResponseResult();
        //判断验证码
/*        boolean isValid = recharge.getVerfiyCode().equals(TencentSms.smsCache.get(recharge.getPhone()));
        if (!isValid) {
            logger.info("验证码错误");
            throw new ParamsCheckException(ErrorCodeDesc.failed.getValue(), "验证码错误");
        }*/
        Date date = new Date();
        UserWallet userWallet = userWalletService.selectByPrimaryUserId(user.getId());
        recharge.setId(null);
        recharge.setCreateTime(date);
        recharge.setOrderNo(OrderUtils.getOrderNo(OrderUtils.RECHARGE,user.getId()));
        recharge.setuId(user.getId());
        recharge.setAvailableAmount(userWallet.getAvailableAmount());
        if(ChannelTypeDesc.offline.toString().equals(recharge.getChannelType())){
            recharge.setStates(RechargeStatusDesc.initial.toString());
            recharge.setMethod(MethodDesc.offline.toString());
            rechargeMapper.insertSelective(recharge);
            //TencentSms.smsCache.remove(recharge.getPhone());
            return responseResult;
        }else{

            String conf = commonConstants.getDictionarysBy(EnvConstants.RECHARGE_CONF,EnvConstants.RECHARGE_TEST_AMOUNT_SWITCH);
            if("1".equals(conf)){
                recharge.setAmount(new BigDecimal("0.01"));
            }
            if(ChannelTypeDesc.wxpay.toString().equals(recharge.getChannelType())){
                recharge.setMethod(MethodDesc.wx_jsapi.toString());//微信默认公众号支付方式
            }
            PayResp payResp = payUtils.preOrder(recharge);
            //下单成功
            logger.info("下单请求应答结果:"+payResp);
            if(payResp.getCode().intValue()==100&&"0000".equals(payResp.getData().getResult_code())){
                recharge.setStates(RechargeStatusDesc.processing.toString());
                recharge.setPayState("0");//未支付
                recharge.setOutTradeNo(payResp.getData().getOut_trade_no());
                String codeUrl = payResp.getData().getCode_url();
                recharge.setCodeUrl(codeUrl);
                rechargeMapper.insertSelective(recharge);

                if(ChannelTypeDesc.alipay.toString().equals(recharge.getChannelType())){
                    //String alipayqr = "alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=";
                    String h5Param = codeUrl.substring(codeUrl.indexOf("url=")+4);
                    //alipayqr+=h5Param;
                    String qrcode = URLDecoder.decode(h5Param,"UTF-8");
                    recharge.setCodeUrl(qrcode);
                    recharge.setH5Url(codeUrl);
                }

                responseResult.addBody("recharge", recharge);
                return responseResult;
            }else{
                responseResult.setErrorCode(ErrorCodeDesc.failed.getValue());
                responseResult.setErrorMessage("提交订单异常~");
                return responseResult;
            }
        }


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

    @Override
    public Long payNotify(NotifyResp notifyResp) {
        Date date = new Date();
        Recharge recharge = rechargeMapper.selectByOrderNo(notifyResp.getU_out_trade_no());
        if(recharge!=null&&!RechargeStatusDesc.success.toString().equals(recharge.getStates())){
            long total =recharge.getAmount().multiply(new BigDecimal("100")).longValue();
            if(notifyResp.getTotal_fee()==total){

                UserWallet updateWallet = new UserWallet();
                updateWallet.setAvailableAmount(recharge.getAmount());
                updateWallet.setuId(recharge.getuId());
                userWalletService.updateWalletAmount(updateWallet);
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
                Recharge updateRecharge = new Recharge();
                updateRecharge.setId(recharge.getId());
                updateRecharge.setStates(RechargeStatusDesc.success.toString());
                updateRecharge.setUpdateTime(date);
                updateRecharge.setPayState(notifyResp.getStatus());
                updateRecharge.setTransactionId(notifyResp.getTransaction_id());
                rechargeMapper.updateByPrimaryKeySelective(updateRecharge);
                //通知前端更新用户金额
                return recharge.getuId();
            }else{
                logger.error("金额不对~");
                Recharge updateRecharge = new Recharge();
                updateRecharge.setId(recharge.getId());
                updateRecharge.setUpdateTime(date);
                updateRecharge.setPayState(notifyResp.getStatus());
                updateRecharge.setRemarks("支付平台回调金额不对等~");
                rechargeMapper.updateByPrimaryKeySelective(updateRecharge);
            }
        }

        return null;
    }
}
