package com.neighbor.app.recharge.service;

import com.neighbor.app.recharge.entity.Recharge;
import com.neighbor.app.recharge.po.RechargeRecordResp;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.ResponseResult;

public interface RechargeService {

    public ResponseResult recharge(UserInfo user, Recharge recharge) throws Exception;

    public RechargeRecordResp rechargeRecord(UserInfo user, Recharge req) throws Exception;

    public ResponseResult rechargeInfo(Recharge recharge)  throws Exception;
}
