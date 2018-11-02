package com.neighbor.security.permission.service;

import java.util.List;

import com.neighbor.security.permission.entity.SysPermission;

public interface SysPermissionService {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysPermission record);

	List<SysPermission> listByRoleId(Integer roleId);

}