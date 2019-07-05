package com.neighbor.common.dictionary.service;

import java.util.List;

import com.neighbor.common.dictionary.entity.Dictionary;
import com.neighbor.common.util.ResponseResult;

public interface DictionaryService {
    int deleteByPrimaryKey(Long id);

    int insertSelective(Dictionary record);

    Dictionary selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Dictionary record);

	List<Dictionary> selectBySelective(Dictionary record);
	
	Long selectPageTotalCount(Dictionary record);

	List<Dictionary> selectPageByObjectForList(Dictionary record);

	void updateByPrimaryKeySelectiveIncludeCache(Dictionary dictionary);


    ResponseResult gameTypeList();

    ResponseResult dictionaryList(String bizCode,boolean cache);
}