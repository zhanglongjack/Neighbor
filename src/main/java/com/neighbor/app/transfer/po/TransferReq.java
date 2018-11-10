package com.neighbor.app.transfer.po;

import java.util.StringJoiner;

public class TransferReq  {

	private long uId;
	private String payPwd;
	private String amount;
	private Long transferUserId;
	private String remarks;

	public long getuId() {
		return uId;
	}

	public void setuId(long uId) {
		this.uId = uId;
	}

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Long getTransferUserId() {
		return transferUserId;
	}

	public void setTransferUserId(Long transferUserId) {
		this.transferUserId = transferUserId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	@Override
	public String toString() {
		return new StringJoiner(", ", TransferReq.class.getSimpleName() + "[", "]")
				.add("uId=" + uId)
				.add("payPwd='" + payPwd + "'")
				.add("amount='" + amount + "'")
				.add("transferUserId='" + transferUserId + "'")
				.add("remarks='" + remarks + "'")
				.toString();
	}
}
