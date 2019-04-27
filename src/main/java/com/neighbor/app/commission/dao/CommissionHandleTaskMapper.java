package com.neighbor.app.commission.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.app.commission.entity.CommissionHandleTask;

@Mapper
public interface CommissionHandleTaskMapper {

    int insert(CommissionHandleTask record);

    CommissionHandleTask selectByPrimaryKey(Long commissionId);

    int updateByPrimaryKeySelective(CommissionHandleTask record);

    List<CommissionHandleTask> selectTaskByStatus(Integer status);

	CommissionHandleTask selectByPrimaryKeyForLock(Long commissionId);
}