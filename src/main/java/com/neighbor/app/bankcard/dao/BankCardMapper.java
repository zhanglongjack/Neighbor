package com.neighbor.app.bankcard.dao;

import com.neighbor.app.bankcard.entity.BankCard;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BankCardMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(BankCard record);

    BankCard selectByPrimaryKey(Long id);

    BankCard selectByBankCardNo(String bankCardNo);

    int updateByPrimaryKeySelective(BankCard record);

    Long selectPageTotalCount(BankCard record);

    List<BankCard> selectPageByObjectForList(BankCard record);

    List<BankCard> selectAll();


}