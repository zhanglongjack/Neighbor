package com.neighbor.app.transfer.service;

import java.util.List;

import com.neighbor.app.transfer.entity.Transfer;

public interface TransferService {
	public List<Transfer> queryAll() throws Exception;
}
