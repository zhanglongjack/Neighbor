package com.neighbor.app.transfer.service;


import com.neighbor.app.transfer.entity.Transfer;
import com.neighbor.app.transfer.po.TransferReq;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.ResponseResult;

public interface TransferService {
	public ResponseResult transfer(UserInfo user , TransferReq req) throws Exception;

	public ResponseResult transferRecord(UserInfo user, Transfer transfer)  throws Exception;

	public ResponseResult transferInfo(Transfer transfer)  throws Exception ;

	public ResponseResult transferForChat(UserInfo user, TransferReq transfer) throws Exception;

	public ResponseResult transferReceive(UserInfo user, TransferReq transfer) throws Exception;

	public ResponseResult transferBackJob() throws Exception;
	public ResponseResult transferBack(Transfer transfer) throws Exception;
}
