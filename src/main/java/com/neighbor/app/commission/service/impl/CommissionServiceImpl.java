package com.neighbor.app.commission.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.neighbor.app.commission.dao.UserCommissionMapper;
import com.neighbor.app.commission.entity.UserCommission;
import com.neighbor.app.commission.service.CommissionService;

@Service
public class CommissionServiceImpl implements CommissionService{
	
	@Autowired
	private UserCommissionMapper userCommissionMapper;

	@Override
	public int insertSelective(UserCommission commission) {
		return userCommissionMapper.insertSelective(commission);
	}

	@Override
	public UserCommission selectByPrimaryKey(Long id) {
		return userCommissionMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(UserCommission commission) {
		return userCommissionMapper.updateByPrimaryKeySelective(commission);
	}

	@Override
	public Long selectPageTotalCount(UserCommission commission) {
		return userCommissionMapper.selectPageTotalCount(commission);
	}

	@Override
	public List<UserCommission> selectPageByObjectForList(UserCommission commission) {
		return userCommissionMapper.selectPageByObjectForList(commission);
	}

}
