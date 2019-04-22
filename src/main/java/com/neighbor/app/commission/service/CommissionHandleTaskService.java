package com.neighbor.app.commission.service;

import java.util.List;

import com.neighbor.app.commission.entity.CommissionHandleTask;

public interface CommissionHandleTaskService {

    int insert(CommissionHandleTask record);

    CommissionHandleTask selectByPrimaryKey(Long commissionId);

    int updateByPrimaryKeySelective(CommissionHandleTask record);

    List<CommissionHandleTask> selectTaskByStatus(Integer status);

	void handleSplitCommission(CommissionHandleTask handleData);
}