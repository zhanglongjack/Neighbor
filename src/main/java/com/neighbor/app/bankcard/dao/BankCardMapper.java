package com.neighbor.app.bankcard.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.app.bankcard.entity.BankCard;
@Mapper
public interface BankCardMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(BankCard record);

    BankCard selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BankCard record);
 
    
	Long selectPageTotalCount(BankCard record);

	List<BankCard> selectPageByObjectForList(BankCard record);

	List<BankCard> selectAll();
}