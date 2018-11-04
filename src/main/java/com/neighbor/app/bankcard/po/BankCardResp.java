package com.neighbor.app.bankcard.po;

import com.neighbor.app.api.common.BaseApiResp;

public class BankCardResp extends BaseApiResp {
	private String bankCardNo;

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

}
