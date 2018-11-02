package com.neighbor.security.role.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.neighbor.security.role.dao.SysUserRoleMapper;
import com.neighbor.security.role.entity.SysUserRoleKey;
import com.neighbor.security.role.service.SysUserRoleService;

@Component
public class SysUserRoleServiceImpl implements SysUserRoleService {

	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;
	
	@Override
	public int deleteByPrimaryKey(SysUserRoleKey key) {
		return sysUserRoleMapper.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(SysUserRoleKey record) {
		return sysUserRoleMapper.insert(record);
	}

	@Override
	public List<SysUserRoleKey> listByUserId(Long getuId) {
		return sysUserRoleMapper.listByUserId(getuId);
	}
}