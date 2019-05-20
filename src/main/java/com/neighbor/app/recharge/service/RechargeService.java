package com.neighbor.app.recharge.service;

import java.util.List;

import com.neighbor.app.pay.entity.NotifyResp;
import com.neighbor.app.recharge.entity.Recharge;
import com.neighbor.app.recharge.po.RechargeRecordResp;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.ResponseResult;

public interface RechargeService {

    public ResponseResult recharge(UserInfo user, Recharge recharge) throws Exception;

//    public RechargeRecordResp rechargeRecord(UserInfo user, Recharge req) throws Exception;

    public ResponseResult rechargeInfo(Recharge recharge)  throws Exception;

	public Long selectPageTotalCount(Recharge detail);

	public List<Recharge> selectPageByObjectForList(Recharge detail);

    ResponseResult modifyRecharge(UserInfo user, Recharge recharge);

    Long payNotify(NotifyResp notifyResp);
}
