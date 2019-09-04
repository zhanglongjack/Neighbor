package com.neighbor.app.pay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.neighbor.app.recharge.constants.RechargeStatusDesc;
import com.neighbor.app.recharge.entity.Recharge;
import com.neighbor.app.recharge.service.RechargeService;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.common.security.EncodeData;
import com.neighbor.common.util.DateUtils;
import com.neighbor.common.util.HttpClientUtils;
import com.neighbor.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Service("pay_wsjfgl")
public class WsjfglPayService implements PayService {
    private final static Logger logger = LoggerFactory.getLogger(WsjfglPayService.class);
    private final static String payUrl = "/Pay_Index.html";
    private final static String queryUrl = "/Pay_Trade_query.html";
    private final static String view = "paymentWsjfgl";
    private final static String channelType = "wsjfgl";

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
        payConfig.init(false,channelType);
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("pay_memberid",payConfig.getAppid());
        hashMap.put("pay_orderid",recharge.getOrderNo());
        hashMap.put("pay_applydate", DateUtils.getStringDate());
        hashMap.put("pay_bankcode",recharge.getChannelNo());
        hashMap.put("pay_notifyurl",payConfig.getNotifyUrl());
        hashMap.put("pay_callbackurl",payConfig.getCallBackUrl());
        long total = recharge.getAmount().longValue();
        hashMap.put("pay_amount",total+"");
        String signStr =  EncodeData.encode(PayUtils.putPairsSequenceAndTogether(hashMap)+"&key="+payConfig.getAppkey()).toUpperCase();
        hashMap.put("pay_md5sign",signStr);
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        hashMap.put("pay_attach", uuid);
        hashMap.put("payUrl",payConfig.getApiUrl()+payUrl);
        String reqStr = JSON.toJSONString(hashMap);
        logger.info(view+" req str ==> "+reqStr);
        recharge.setCodeUrl(reqStr);

    }

    @Override
    public QueryOrderResp queryOrder(Recharge recharge) throws Exception {
        payConfig.init(false,channelType);
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("pay_memberid",payConfig.getAppid());
        hashMap.put("pay_orderid",recharge.getOrderNo());
        String signStr =  EncodeData.encode(PayUtils.putPairsSequenceAndTogether(hashMap)+"&key="+payConfig.getAppkey()).toUpperCase();
        hashMap.put("pay_md5sign",signStr);
        String respStr = HttpClientUtils.httpPostWithPAaram(payConfig.getApiUrl()+queryUrl,hashMap);
        JSONObject json = JSON.parseObject(respStr);
        QueryOrderResp queryOrderResp = new QueryOrderResp();
        queryOrderResp.setMessage("支付通道返回失败，原因需要咨询第三方！");
        queryOrderResp.setOrderStatus(1);//订单异常
        if("00".equals(json.getString("returncode"))){
            String trade_state = json.getString("trade_state");
            if("NOTPAY".equalsIgnoreCase(trade_state)){
                queryOrderResp.setMessage("订单还没有支付成功！");
            }else if("SUCCESS".equalsIgnoreCase(trade_state)){
                queryOrderResp.setOrderStatus(2);//支付成功
            }
        }
        return queryOrderResp;
    }

    @Override
    public PayNotifyResp payNotify(HashMap<String, String> headerMap, HashMap<String, String> paramMap, String reqBody) throws Exception {
        payConfig.init(false,channelType);
        PayNotifyResp payNotifyResp = new PayNotifyResp();
        payNotifyResp.setAck("OK");
        Date date = new Date();
        String attach = paramMap.remove("attach");
        BigDecimal availableAmount = new BigDecimal(paramMap.get("amount"));
        String transactionId = paramMap.get("transaction_id");
        if(validParam(paramMap)){
            String orderid = paramMap.get("orderid");
            String returncode = paramMap.get("returncode");
            Recharge recharge = rechargeService.selectByOrderNo(orderid);
            if(!JSON.parseObject(recharge.getCodeUrl()).getString("pay_attach").equals(attach)){
                logger.error("预留信息错误~");
                return payNotifyResp;
            }
            //4已结算,1已支付
            if(!"00".equals(returncode)){
                logger.error("订单状态失败~");
                Recharge updateRecharge = new Recharge();
                updateRecharge.setId(recharge.getId());
                updateRecharge.setUpdateTime(date);
                updateRecharge.setTransactionId(transactionId);
                updateRecharge.setRemarks("支付通道返回失败，原因需要咨询第三方！");
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
                updateRecharge.setTransactionId(transactionId);
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
                updateRecharge.setTransactionId(transactionId);
                updateRecharge.setChannelType(recharge.getChannelType());
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
        String memberid = paramMap.get("memberid");
        if(!payConfig.getAppid().equals(memberid)){
            return false;
        }
        String sign = paramMap.remove("sign");
        String signStr =  EncodeData.encode(PayUtils.putPairsSequenceAndTogether(paramMap)+"&key="+payConfig.getAppkey()).toUpperCase();
        return sign.equals(signStr);
    }
}
