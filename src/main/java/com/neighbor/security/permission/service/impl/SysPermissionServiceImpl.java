package com.neighbor.security.permission.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.neighbor.security.permission.dao.SysPermissionMapper;
import com.neighbor.security.permission.entity.SysPermission;
import com.neighbor.security.permission.service.SysPermissionService;

@Component
public class SysPermissionServiceImpl implements SysPermissionService {

	@Autowired
	private SysPermissionMapper permissionMapper;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return permissionMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(SysPermission record) {
		return permissionMapper.insertSelective(record);
	}

	@Override
	public SysPermission selectByPrimaryKey(Integer id) {
		return permissionMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SysPermission record) {
		return permissionMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<SysPermission> listByRoleId(Integer roleId) {
		return permissionMapper.listByRoleId(roleId);
	}
 

}