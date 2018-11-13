package com.neighbor.app.balance.service;

import java.util.List;

import com.neighbor.app.balance.entity.BalanceDetail;
public interface BalanceDetailService {
    int deleteByPrimaryKey(Long id);

    int insertSelective(BalanceDetail record);

    BalanceDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BalanceDetail record);
    
	Long selectPageTotalCount(BalanceDetail record);

	List<BalanceDetail> selectPageByObjectForList(BalanceDetail record);

	List<BalanceDetail> selectAll();
}