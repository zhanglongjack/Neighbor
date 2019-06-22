package com.neighbor.common.dictionary.service.impl;

import java.util.*;

import com.neighbor.common.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.neighbor.common.constants.CommonConstants;
import com.neighbor.common.dictionary.dao.DictionaryMapper;
import com.neighbor.common.dictionary.entity.Dictionary;
import com.neighbor.common.dictionary.service.DictionaryService;

@Service
public class DictionaryServiceImpl implements DictionaryService {

	@Autowired
	private DictionaryMapper dictionaryMapper;
	@Autowired
	private CommonConstants commonConstants;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return dictionaryMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(Dictionary record) {
		return dictionaryMapper.insertSelective(record);
	}

	@Override
	public Dictionary selectByPrimaryKey(Long id) {
		return dictionaryMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Dictionary record) {
		return dictionaryMapper.updateByPrimaryKeySelective(record);
	} 
	
	@Override
	public List<Dictionary> selectBySelective(Dictionary record) {
		return dictionaryMapper.selectBySelective(record);
	}

	@Override
	public Long selectPageTotalCount(Dictionary record) {
		return dictionaryMapper.selectPageTotalCount(record);
	}

	@Override
	public List<Dictionary> selectPageByObjectForList(Dictionary record) {
		return dictionaryMapper.selectPageByObjectForList(record);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void updateByPrimaryKeySelectiveIncludeCache(Dictionary dictionary) {
		updateByPrimaryKeySelective(dictionary);
		commonConstants.updateDictionaryBy(dictionary.getBizCode(),dictionary.getCode(),dictionary.getName());
	}


	@Override
	public ResponseResult gameTypeList() {
		Map<String,String> map = commonConstants.getDictionarysByKey("gameType");
		Dictionary[] arr = new Dictionary[map.size()];
		for(String key : map.keySet()){
			int i = Integer.valueOf(key);
			Dictionary dictionary = new Dictionary();
			dictionary.setCode(key);
			dictionary.setName(map.get(key));
			arr[i-1]=dictionary;
		}
		ResponseResult result = new ResponseResult();
		result.addBody("resultList",arr);
		return result;
	}

}