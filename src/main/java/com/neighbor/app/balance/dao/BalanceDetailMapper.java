package com.neighbor.app.balance.dao;

import java.util.List;

import com.neighbor.app.balance.entity.BalanceDetail;

public interface BalanceDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(BalanceDetail record);

    BalanceDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BalanceDetail record);
    
	Long selectPageTotalCount(BalanceDetail record);

	List<BalanceDetail> selectPageByObjectForList(BalanceDetail record);

	List<BalanceDetail> selectAll();
}