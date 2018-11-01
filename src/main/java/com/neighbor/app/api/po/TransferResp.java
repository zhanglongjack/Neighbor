package com.neighbor.app.api.po;

import com.neighbor.app.api.common.BaseApiResp;

public class TransferResp extends BaseApiResp {
	private String orderNo;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

}
