package com.neighbor.app.bankcard.dao;

import com.neighbor.app.bankcard.entity.BankCard;

public interface BankCardMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(BankCard record);

    BankCard selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BankCard record);
 
}