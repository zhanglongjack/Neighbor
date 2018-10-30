package com.neighbor.app.recharge.dao;

import java.util.List;

import com.neighbor.app.recharge.entity.Recharge;

public interface RechargeMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(Recharge record);

    Recharge selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Recharge record);
    
	Long selectPageTotalCount(Recharge record);

	List<Recharge> selectPageByObjectForList(Recharge record);

	List<Recharge> selectAll();
}