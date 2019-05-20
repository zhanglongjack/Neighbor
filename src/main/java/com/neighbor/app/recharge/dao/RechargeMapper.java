package com.neighbor.app.recharge.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.app.recharge.entity.Recharge;
@Mapper
public interface RechargeMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(Recharge record);

    Recharge selectByPrimaryKey(Long id);

    Recharge selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(Recharge record);
    
	Long selectPageTotalCount(Recharge record);

	List<Recharge> selectPageByObjectForList(Recharge record);

	List<Recharge> selectAll();
}