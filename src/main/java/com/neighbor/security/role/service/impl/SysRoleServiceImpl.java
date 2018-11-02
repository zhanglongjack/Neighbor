package com.neighbor.security.role.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.neighbor.security.permission.entity.SysPermission;
import com.neighbor.security.role.dao.SysRoleMapper;
import com.neighbor.security.role.entity.SysRole;
import com.neighbor.security.role.service.SysRoleService;

@Component
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	private SysRoleMapper roleMapper;
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return roleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(SysRole record) {
		return roleMapper.insertSelective(record);
	}

	@Override
	public SysRole selectByPrimaryKey(Integer id) {
		return roleMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SysRole record) {
		return roleMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public SysPermission selectByName(String roleName) {
		return roleMapper.selectByName(roleName);
	}
}