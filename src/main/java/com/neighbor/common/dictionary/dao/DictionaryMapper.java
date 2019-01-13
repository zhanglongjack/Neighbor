package com.neighbor.common.dictionary.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.common.dictionary.entity.Dictionary;

@Mapper
public interface DictionaryMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(Dictionary record);

    Dictionary selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Dictionary record);

	List<Dictionary> selectBySelective(Dictionary record);
	
	Long selectPageTotalCount(Dictionary record);

	List<Dictionary> selectPageByObjectForList(Dictionary record);

}