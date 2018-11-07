package com.neighbor.app.transfer.service.impl;

import java.util.List;

import com.neighbor.app.transfer.po.TransferRecord;
import com.neighbor.app.transfer.po.TransferRecordReq;
import com.neighbor.app.transfer.po.TransferRecordResp;
import com.neighbor.app.transfer.po.TransferReq;
import com.neighbor.app.users.service.UserService;
import com.neighbor.common.util.PageTools;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neighbor.app.transfer.dao.TransferMapper;
import com.neighbor.app.transfer.entity.Transfer;
import com.neighbor.app.transfer.service.TransferService;

@Service
public class TransferServiceImpl implements TransferService {

	@Autowired
	private TransferMapper transferMapper;

    @Autowired
	private UserService userService;

    /**
     * app 查询转账记录
     * @param req
     * @return
     * @throws Exception
     */
	@Override
	public TransferRecordResp queryPage(TransferRecordReq req) throws Exception {
		TransferRecordResp transferRecordResp = new TransferRecordResp();
		Transfer transfer = new Transfer();
		BeanUtils.copyProperties(req,transfer);
		PageTools pageTools = new PageTools();
		pageTools.setIndex(req.getPageIndex());
		pageTools.setPageSize(req.getPageSize());
		transfer.setPageTools(pageTools);
		Long total = transferMapper.selectPageTotalCountForApp(transfer);
		List<TransferRecord> transfers = transferMapper.selectPageForApp(transfer);
		transferRecordResp.setTotalNum(total);
		transferRecordResp.setPageList(transfers);
		return transferRecordResp;
	}

    /**
     * 内部转账方法
     * @param req
     * @return
     * @throws Exception
     */
	@Override
	public TransferRecordResp transfer(TransferReq req) throws Exception {

		return null;
	}
}
