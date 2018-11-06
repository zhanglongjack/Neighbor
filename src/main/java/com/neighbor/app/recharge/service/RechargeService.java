package com.neighbor.app.recharge.service;

import com.neighbor.app.recharge.po.RechargeRecordReq;
import com.neighbor.app.recharge.po.RechargeRecordResp;

public interface RechargeService {
    public RechargeRecordResp queryPage(RechargeRecordReq req) throws Exception;
}
