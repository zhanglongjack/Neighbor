package com.neighbor.app.bankcard.service;

import com.neighbor.app.bankcard.entity.BankCard;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.ResponseResult;

public interface BankCardService {

    public ResponseResult addCard(UserInfo user, BankCard bankCard) throws Exception;

    public ResponseResult listRecord(UserInfo user, BankCard bankCard)throws Exception;
}
