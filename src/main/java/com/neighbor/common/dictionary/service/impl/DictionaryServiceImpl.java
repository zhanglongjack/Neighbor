package com.neighbor.common.dictionary.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neighbor.common.dictionary.dao.DictionaryMapper;
import com.neighbor.common.dictionary.entity.Dictionary;
import com.neighbor.common.dictionary.service.DictionaryService;

@Service
public class DictionaryServiceImpl implements DictionaryService {

	@Autowired
	private DictionaryMapper dictionaryMapper;
	
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
	

}