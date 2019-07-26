package com.test;

import com.alibaba.fastjson.JSON;
import com.neighbor.StartNeighbor;
import com.neighbor.app.common.util.OrderUtils;
import com.neighbor.app.pay.entity.QueryOrderResp;
import com.neighbor.app.pay.service.AlipayService;
import com.neighbor.app.recharge.entity.Recharge;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartNeighbor.class)
public class AlipayServiceTester {

    @Autowired
    private AlipayService alipayService;

    @Test
    public void preOrder(){
        Recharge recharge = new Recharge();
        try {
            recharge.setOrderNo(OrderUtils.getOrderNo(OrderUtils.RECHARGE,6000000L));
            recharge.setAmount(new BigDecimal("0.01"));
            recharge.setBody("积分充值 1分");
            alipayService.preOrder(recharge);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void validParam(){
        String jsonStr = "{\n" +
                "  \"gmt_create\": \"2019-07-25 22:35:43\",\n" +
                "  \"charset\": \"UTF-8\",\n" +
                "  \"seller_email\": \"18345041520\",\n" +
                "  \"subject\": \"积分充值：0.01元\",\n" +
                "  \"sign\": \"kgFvgAVD3S0nt3jlOKpIBpSsFrsLixR3Y6GFy1hrxtqJ5gue6BTrRw5+cgdRCZGsmUHl515j61kTqDO/glbbj0B9z0Fi/Jab+065kHW8xnYA1przKsltVgUQWE4gkYo3ZXkeLAH287+zrkj5Z4ynjFvx+xbykeCBmVtb4ZZq0w17voio9PPubZKZQJYqFWaL/HJAzrs89BviORlvEC3yMn9k7VQQoskwHDm+WlgPhyBojcqLJUQEmJy22O+N7OErV340T8ks83X8FaxsOpJNXphFXLFyT89I7BBod1EuJR6wgNsRSevLac+A0ieUZxULgybjL84bZcWsAZ8Fv/Jqhw==\",\n" +
                "  \"buyer_id\": \"2088802788364260\",\n" +
                "  \"invoice_amount\": \"0.01\",\n" +
                "  \"notify_id\": \"2019072500222223545064260569680853\",\n" +
                "  \"fund_bill_list\": \"[{\\\"amount\\\":\\\"0.01\\\",\\\"fundChannel\\\":\\\"ALIPAYACCOUNT\\\"}]\",\n" +
                "  \"notify_type\": \"trade_status_sync\",\n" +
                "  \"trade_status\": \"TRADE_SUCCESS\",\n" +
                "  \"receipt_amount\": \"0.01\",\n" +
                "  \"app_id\": \"2019072565964702\",\n" +
                "  \"buyer_pay_amount\": \"0.01\",\n" +
                "  \"sign_type\": \"RSA2\",\n" +
                "  \"seller_id\": \"2088532778903774\",\n" +
                "  \"gmt_payment\": \"2019-07-25 22:35:44\",\n" +
                "  \"notify_time\": \"2019-07-25 22:49:07\",\n" +
                "  \"version\": \"1.0\",\n" +
                "  \"out_trade_no\": \"260001720190725103531937\",\n" +
                "  \"total_amount\": \"0.01\",\n" +
                "  \"trade_no\": \"2019072522001464260523755886\",\n" +
                "  \"auth_app_id\": \"2019072565964702\",\n" +
                "  \"buyer_logon_id\": \"177****8792\",\n" +
                "  \"point_amount\": \"0.00\"\n" +
                "}";
        HashMap<String,String> map = JSON.parseObject(jsonStr,HashMap.class);
        try {
            System.out.println(">>>>>>>>>>>>>"+alipayService.validParam(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void queryOrder(){
        Recharge recharge = new Recharge();
        try {
            recharge.setOrderNo("260001720190726084735201");
            QueryOrderResp queryOrderResp = alipayService.queryOrder(recharge);
            System.out.println("queryOrder>>>>>>>>>"+JSON.toJSONString(queryOrderResp));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
