package com.neighbor.app.api.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.neighbor.app.api.common.BaseApiResp;
import com.neighbor.app.api.common.Constants;
import com.neighbor.app.transfer.service.TransferService;

@Component(Constants.TRANSFER_REQ)
public class TransferHandler implements CommonHandler {
	
	@Autowired
	private TransferService transferService;

	@Override
	public BaseApiResp service(String requsetBody) throws Exception{
	/*	TransferResp transferResp = new TransferResp();
		TransferReq req = JSON.parseObject(requsetBody,TransferReq.class);
		//TODO 这里写业务逻辑
	    transferResp.setOrderNo("no.1111111 + ");*/
		return null;
	}
	
	public static void main(String[] args) {
	/*	TransferReq req = new TransferReq();
		req.setServiceName("transfer_req");
		req.setAmount("1000");
		req.setPayPwd("123456789");
		req.setuId(123456);
		req.setPayPwd("xxxxxx");
		req.setRemarks("测试转账");
		req.setTransferUserId("66666");
		System.out.println(JSON.toJSON(req));*/
	}

}
