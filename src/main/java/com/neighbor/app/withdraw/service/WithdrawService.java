package com.neighbor.app.withdraw.service;

import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.withdraw.entity.Withdraw;
import com.neighbor.common.util.ResponseResult;

public interface WithdrawService {
    ResponseResult withdraw(UserInfo user, Withdraw withdraw) throws Exception;

    ResponseResult withdrawRecord(UserInfo user, Withdraw withdraw) throws Exception;

    ResponseResult withdrawInfo(Withdraw withdraw)  throws Exception;

    ResponseResult preWithdraw(UserInfo user, Withdraw withdraw)  throws Exception;
}
