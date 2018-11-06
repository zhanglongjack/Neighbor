package com.neighbor.app.recharge.service.impl;

import com.neighbor.app.recharge.dao.RechargeMapper;
import com.neighbor.app.recharge.entity.Recharge;
import com.neighbor.app.recharge.po.RechargeRecord;
import com.neighbor.app.recharge.po.RechargeRecordReq;
import com.neighbor.app.recharge.po.RechargeRecordResp;
import com.neighbor.app.recharge.service.RechargeService;
import com.neighbor.common.util.PageTools;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class RechargeServiceImpl implements RechargeService {

    @Autowired
    private RechargeMapper rechargeMapper;

    @Override
    public RechargeRecordResp queryPage(RechargeRecordReq req) throws Exception {
        RechargeRecordResp rechargeRecordResp = new RechargeRecordResp();
        Recharge recharge = new Recharge();
        BeanUtils.copyProperties(req,recharge);
        PageTools pageTools = new PageTools();
        pageTools.setIndex(req.getPageIndex());
        pageTools.setPageSize(req.getPageSize());
        recharge.setPageTools(pageTools);
        Long total = rechargeMapper.selectPageTotalCount(recharge);
        List<Recharge> recharges = rechargeMapper.selectPageByObjectForList(recharge);
        List<RechargeRecord> pageList = new ArrayList<RechargeRecord>();
        if(recharges!=null&&pageList.size()>0){
            for(Recharge rec : recharges){
                RechargeRecord rechargeRecord = new RechargeRecord();
                BeanUtils.copyProperties(rec,rechargeRecord);
                pageList.add(rechargeRecord);
            }
        }
        rechargeRecordResp.setTotalNum(total);
        rechargeRecordResp.setPageList(pageList);
        return rechargeRecordResp;
    }
}
