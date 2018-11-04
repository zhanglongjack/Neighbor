package com.neighbor.app.bankcard.service;


import java.util.List;

import com.neighbor.app.bankcard.entity.BankCard;

public interface BankCardService {
    int deleteByPrimaryKey(Long id);

    int insertSelective(BankCard record);

    BankCard selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BankCard record);
    
    BankCard selectBankCard(Long uId);
    
	Long selectPageTotalCount(BankCard record);

	List<BankCard> selectPageByObjectForList(BankCard record);

	List<BankCard> selectAll();
}
