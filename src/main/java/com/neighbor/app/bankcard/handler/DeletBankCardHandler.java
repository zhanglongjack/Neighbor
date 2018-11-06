package com.neighbor.app.bankcard.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.neighbor.app.api.common.BaseApiResp;
import com.neighbor.app.api.common.Constants;
import com.neighbor.app.api.handler.CommonHandler;
import com.neighbor.app.bankcard.po.BankCardReq;
import com.neighbor.app.bankcard.po.BankCardResp;
import com.neighbor.app.bankcard.service.BankCardService;

@Component(Constants.DELETEBANKCARD_REQ)
public class DeletBankCardHandler implements CommonHandler{
	
	@Autowired
	private BankCardService bankCardService;
	
	@Override
	public BaseApiResp service(String requsetBody) throws Exception {
		
		BankCardReq req = JSON.parseObject(requsetBody,BankCardReq.class);
		bankCardService.deleteByPrimaryKey((long)req.getuId());
		
		BankCardResp bankCardResp = new BankCardResp();
		bankCardResp.setServiceName(Constants.DELETEBANKCARD_REQ);
		bankCardResp.setErrorCode(0);
		return bankCardResp;
	}	
}
