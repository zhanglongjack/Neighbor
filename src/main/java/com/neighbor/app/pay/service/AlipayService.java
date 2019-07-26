package com.neighbor.app.pay.service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.neighbor.app.balance.entity.BalanceDetail;
import com.neighbor.app.balance.po.TransactionItemDesc;
import com.neighbor.app.balance.po.TransactionSubTypeDesc;
import com.neighbor.app.balance.po.TransactionTypeDesc;
import com.neighbor.app.balance.service.BalanceDetailService;
import com.neighbor.app.pay.common.PayConfig;
import com.neighbor.app.pay.common.PayService;
import com.neighbor.app.pay.common.PayUtils;
import com.neighbor.app.pay.constants.PayAction;
import com.neighbor.app.pay.entity.PayNotifyResp;
import com.neighbor.app.pay.entity.QueryOrderResp;
import com.neighbor.app.pay.entity.alipay.OrderReq;
import com.neighbor.app.pay.entity.alipay.QueryOrderReq;
import com.neighbor.app.recharge.constants.RechargeStatusDesc;
import com.neighbor.app.recharge.entity.Recharge;
import com.neighbor.app.recharge.service.RechargeService;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.common.security.EncodeData;
import com.neighbor.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
@Service
public class AlipayService implements PayService {
    private final static Logger logger = LoggerFactory.getLogger(AlipayService.class);
    private final static String format = "json";
    private final static String charset = "UTF-8";
    private final static String sign_type = "RSA2";
    private static volatile AlipayClient alipayClient = null;
    private final static String TRADE_SUCCESS = "TRADE_SUCCESS";//交易支付成功

    @Autowired
    private PayConfig payConfig;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private UserWalletService userWalletService;

    @Autowired
    private BalanceDetailService balanceDetailService;


    //两次的判断都是有需要的
    private AlipayClient getAlipayClient() {
        payConfig.init(false);
        if(alipayClient==null){
            synchronized(AlipayService.class){
                if(alipayClient==null){
                    alipayClient = new DefaultAlipayClient(payConfig.getApiUrl(),payConfig.getAppid(),payConfig.getAppkey(),format,charset,payConfig.getAlipayPubKey(),sign_type);
                }
            }
        }
        return alipayClient;
    }


    @Override
    public String viewName() {
        return null;
    }

    @Override
    public void preOrder(Recharge recharge) throws Exception {
        //构造data参数 加密
        payConfig.init(false);
        logger.info("请求支付宝下单...");
        AlipayClient alipayClient = getAlipayClient();
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setNotifyUrl(payConfig.getNotifyUrl());
        request.setReturnUrl(payConfig.getCallBackUrl());
        OrderReq orderReq = new OrderReq();
        orderReq.setOut_trade_no(recharge.getOrderNo());
        orderReq.setSubject(recharge.getBody());
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        orderReq.setTotal_amount(decimalFormat.format(recharge.getAmount()));
        String bizContent = JSON.toJSONString(orderReq);
        logger.info("request bizContent >>> "+bizContent);
        request.setBizContent(bizContent);
        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
        String resp = JSON.toJSONString(response);
        if(response.isSuccess()){
            logger.info("请求支付成功 <<< "+resp);
            recharge.setCodeUrl(response.getBody());
            recharge.setPayAction(PayAction.app.toString());
        } else {
            logger.info("请求支付失败 <<< "+resp);
            throw new Exception("请求支付失败："+response.getMsg());
        }
    }

    @Override
    public QueryOrderResp queryOrder(Recharge recharge) throws Exception {
        QueryOrderResp queryOrderResp = new QueryOrderResp();
        queryOrderResp.setOrderStatus(0);//处理中
        payConfig.init(false);
        logger.info("请求支付宝查询订单...");
        AlipayClient alipayClient = getAlipayClient();
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        QueryOrderReq queryOrderReq = new QueryOrderReq();
        queryOrderReq.setOut_trade_no(recharge.getOrderNo());
        request.setBizContent(JSON.toJSONString(queryOrderReq));
        AlipayTradeQueryResponse response = alipayClient.execute(request);
        if(response.isSuccess()&&TRADE_SUCCESS.equals(response.getTradeStatus())){
            queryOrderResp.setOrderStatus(2);//支付成功
        } else {
            queryOrderResp.setOrderStatus(1);//支付失败
            queryOrderResp.setMessage(response.getMsg());
        }
        return queryOrderResp;
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public PayNotifyResp payNotify(HashMap<String, String> headerMap, HashMap<String, String> paramMap, String reqBody) throws Exception {
        PayNotifyResp payNotifyResp = new PayNotifyResp();
        payNotifyResp.setAck("success");
        Date date = new Date();
        if(validParam(paramMap)){
            String orderid = paramMap.get("out_trade_no");
            String returncode = paramMap.get("trade_status");
            Recharge recharge = rechargeService.selectByOrderNo(orderid);
            if(!TRADE_SUCCESS.equals(returncode)){
                logger.error("订单状态失败~");
                Recharge updateRecharge = new Recharge();
                updateRecharge.setId(recharge.getId());
                updateRecharge.setUpdateTime(date);
                updateRecharge.setOutTradeNo(paramMap.get("trade_no"));
                updateRecharge.setRemarks(paramMap.get("msg"));
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
                updateRecharge.setOutTradeNo(paramMap.get("trade_no"));
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
                updateRecharge.setOutTradeNo(paramMap.get("trade_no"));
                updateRecharge.setChannelType(recharge.getChannelType());
                BigDecimal availableAmount = new BigDecimal(paramMap.get("total_amount"));
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

    public boolean validParam(HashMap<String, String> params) throws Exception{
        payConfig.init(false);
        return AlipaySignature.rsaCheckV1(params,payConfig.getAlipayPubKey(),charset,sign_type);
    }

}
