package com.neighbor.app.balance.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.app.balance.entity.BalanceDetail;
@Mapper
public interface BalanceDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(BalanceDetail record);

    BalanceDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BalanceDetail record);
    
	Long selectPageTotalCount(BalanceDetail record);

	List<BalanceDetail> selectPageByObjectForList(BalanceDetail record);

	List<BalanceDetail> selectAll();
}