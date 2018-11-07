package com.neighbor.app.transfer.service;

import java.util.List;

import com.neighbor.app.transfer.entity.Transfer;
import com.neighbor.app.transfer.po.TransferRecordReq;
import com.neighbor.app.transfer.po.TransferRecordResp;
import com.neighbor.app.transfer.po.TransferReq;

public interface TransferService {
	public TransferRecordResp queryPage(TransferRecordReq req) throws Exception;
	public TransferRecordResp transfer(TransferReq req) throws Exception;
}
