package com.neighbor.app.transfer.dao;

import java.util.List;

import com.neighbor.app.transfer.po.TransferRecord;
import org.apache.ibatis.annotations.Mapper;

import com.neighbor.app.transfer.entity.Transfer;
@Mapper
public interface TransferMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(Transfer record);

    Transfer selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Transfer record);
    
    
	Long selectPageTotalCount(Transfer record);


	List<Transfer> selectPageByObjectForList(Transfer record);


	List<Transfer> selectAll();

}