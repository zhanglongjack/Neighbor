package com.neighbor.app.pay.service;

import com.alibaba.fastjson.JSON;
import com.neighbor.app.balance.entity.BalanceDetail;
import com.neighbor.app.balance.po.TransactionItemDesc;
import com.neighbor.app.balance.po.TransactionSubTypeDesc;
import com.neighbor.app.balance.po.TransactionTypeDesc;
import com.neighbor.app.balance.service.BalanceDetailService;
import com.neighbor.app.pay.common.PayConfig;
import com.neighbor.app.pay.common.PayService;
import com.neighbor.app.pay.common.PayUtils;
import com.neighbor.app.pay.entity.PayNotifyResp;
import com.neighbor.app.pay.entity.QueryOrderResp;
import com.neighbor.app.pay.entity.gyf.QueryResp;
import com.neighbor.app.recharge.constants.RechargeStatusDesc;
import com.neighbor.app.recharge.entity.Recharge;
import com.neighbor.app.recharge.service.RechargeService;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.common.security.EncodeData;
import com.neighbor.common.util.HttpClientUtils;
import com.neighbor.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

@Service
public class GyfPayService implements PayService {
    private final static Logger logger = LoggerFactory.getLogger(GyfPayService.class);

    private final static String uNumber = "xT0t5q9i"; //平台唯一识别码，固定值

    private final static String view = "paymentGyf";

    private final static String payUrl = "/trade/pay.trade.pay.action";
    private final static String queryUrl = "/trade/pay.trade.query.action";


    @Autowired
    private PayConfig payConfig;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private UserWalletService userWalletService;

    @Autowired
    private BalanceDetailService balanceDetailService;


    @Override
    public String viewName() {
        return view;
    }

    @Override
    public void preOrder(Recharge recharge) throws Exception {
        //构造data参数 加密
        payConfig.init(false);
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("uNumber",uNumber);
        hashMap.put("merId",payConfig.getAppid());
        hashMap.put("orderId",recharge.getOrderNo());
        long total = recharge.getAmount().multiply(new BigDecimal("100")).longValue();
        hashMap.put("orderAmt",total+"");
        hashMap.put("wlistIp",payConfig.getWlistIp());
        hashMap.put("notifyUrl",payConfig.getNotifyUrl());
        hashMap.put("returnUrl",payConfig.getCallBackUrl());
        hashMap.put("payChannel",recharge.getChannelNo());
        hashMap.put("remark",recharge.getuId()+"");
        String signStr =  EncodeData.encode(PayUtils.putPairsSequenceAndTogether(hashMap)+"&key="+payConfig.getAppkey()).toUpperCase();
        hashMap.put("sign",signStr);
        hashMap.put("payUrl",payConfig.getApiUrl()+payUrl);
        String reqStr = JSON.toJSONString(hashMap);
        logger.info("gyf pay req str ==> "+reqStr);
        recharge.setCodeUrl(reqStr);
    }

    @Override
    public QueryOrderResp queryOrder(Recharge recharge) throws Exception {
        payConfig.init(false);
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("uNumber",uNumber);
        hashMap.put("merId",payConfig.getAppid());
        hashMap.put("merOrderId",recharge.getOrderNo());
        String signStr =  EncodeData.encode(PayUtils.putPairsSequenceAndTogether(hashMap)+"&key="+payConfig.getAppkey()).toUpperCase();
        hashMap.put("sign",signStr);
        String respStr = HttpClientUtils.httpPostWithPAaram(payConfig.getApiUrl()+queryUrl,hashMap);
        QueryResp queryResp = JSON.parseObject(respStr,QueryResp.class);
        QueryOrderResp queryOrderResp = new QueryOrderResp();
        queryOrderResp.setMessage(queryResp.getMessage());
        queryOrderResp.setOrderStatus(queryOrderResp.getOrderStatus());
        return queryOrderResp;
    }

    @Override
    public PayNotifyResp payNotify(HashMap<String, String> headerMap, HashMap<String, String> paramMap, String reqBody) throws Exception {
        payConfig.init(false);
        PayNotifyResp payNotifyResp = new PayNotifyResp();
        payNotifyResp.setAck("success");
        Date date = new Date();
        if(validParam(paramMap)){
            String orderid = paramMap.get("merOrderId");
            String returncode = paramMap.get("orderStatus");
            Recharge recharge = rechargeService.selectByOrderNo(orderid);
            String remark = paramMap.get("remark");
            if(remark.equals(recharge.getuId()+"")){
                logger.error("预留信息错误~");
                return payNotifyResp;
            }
            //4已结算,1已支付
            if(!"1".equals(returncode)&&!"4".equals(returncode)){
                logger.error("订单状态失败~");
                Recharge updateRecharge = new Recharge();
                updateRecharge.setId(recharge.getId());
                updateRecharge.setUpdateTime(date);
                updateRecharge.setTransactionId(paramMap.get("orderId"));
                updateRecharge.setRemarks(paramMap.get("message"));
                rechargeService.updateByPrimaryKeySelective(updateRecharge);
                return payNotifyResp;
            }
            //查询订单状态
            QueryOrderResp queryOrderResp = queryOrder(recharge);
            //订单状态：1 订单冻结 0 处理中， 1 订单异常， 2 支付成功
            if(queryOrderResp.getOrderStatus()!=2){
                logger.error("查询订单状态失败~");
                Recharge updateRecharge = new Recharge();
                updateRecharge.setId(recharge.getId());
                updateRecharge.setUpdateTime(date);
                updateRecharge.setTransactionId(paramMap.get("orderId"));
                updateRecharge.setRemarks("状态："+queryOrderResp.getOrderStatus()+"；"+queryOrderResp.getMessage());
                rechargeService.updateByPrimaryKeySelective(updateRecharge);
                return payNotifyResp;
            }
            if(recharge!=null&&!RechargeStatusDesc.success.toString().equals(recharge.getStates())){
                UserWallet updateWallet = new UserWallet();
                updateWallet.setAvailableAmount(recharge.getAmount());
                updateWallet.setuId(recharge.getuId());
                userWalletService.updateWalletAmount(updateWallet);

                UserWallet userWallet = userWalletService.selectByPrimaryUserId(recharge.getuId());

                //充值交易明细
                BalanceDetail balanceDetail = new BalanceDetail();
                balanceDetail.setAmount(recharge.getAmount());
                balanceDetail.setAvailableAmount(userWallet.getAvailableAmount());
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
                updateRecharge.setPayState("1");//支付成功
                updateRecharge.setTransactionId(paramMap.get("orderId"));
                updateRecharge.setChannelType(recharge.getChannelType());
                BigDecimal availableAmount = new BigDecimal(paramMap.get("merorderAmt")).divide(new BigDecimal("100"));
                updateRecharge.setAvailableAmount(availableAmount);
                rechargeService.updateByPrimaryKeySelective(updateRecharge);
                //通知前端更新用户金额
                payNotifyResp.setuId(recharge.getuId());
                return payNotifyResp;

            }
        }else{
            logger.error("验证签名失败~");
        }

        return payNotifyResp;
    }

    private boolean validParam(HashMap<String, String> paramMap) {
        String uNumber = paramMap.get("uNumber");
        String merId = paramMap.get("merId");
        if(!uNumber.equals(uNumber)){
            return false;
        }
        if(!payConfig.getAppid().equals(merId)){
            return false;
        }
        String sign = paramMap.remove("sign");
        String signStr =  EncodeData.encode(PayUtils.putPairsSequenceAndTogether(paramMap)+"&key="+payConfig.getAppkey()).toUpperCase();
        return sign.equals(signStr);
    }
}
