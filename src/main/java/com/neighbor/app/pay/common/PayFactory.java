package com.neighbor.app.pay.common;

import com.neighbor.app.api.common.SpringUtil;
import com.neighbor.app.pay.constants.PayChannelDesc;
import com.neighbor.common.constants.CommonConstants;
import com.neighbor.common.constants.EnvConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PayFactory {
    @Autowired
    private CommonConstants commonConstants;

    public PayService getService() throws Exception{
        String payChannel = commonConstants.getDictionarysBy(EnvConstants.RECHARGE_PAY_CHANNEL,EnvConstants.RECHARGE_PAY_CHANNEL_CODE);
        PayService payService = (PayService) SpringUtil.getBean(PayChannelDesc.getDesByValue(payChannel));
        return payService;
    }
}
