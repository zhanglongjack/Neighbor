package com.neighbor.app.withdraw.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.app.withdraw.entity.Withdraw;
@Mapper
public interface WithdrawMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(Withdraw record);

    Withdraw selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Withdraw record);
    
    
  	Long selectPageTotalCount(Withdraw record);

  	List<Withdraw> selectPageByObjectForList(Withdraw record);

  	List<Withdraw> selectAll();

 
}