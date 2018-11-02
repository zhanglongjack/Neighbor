package com.neighbor.security.role.service;

import com.neighbor.security.permission.entity.SysPermission;
import com.neighbor.security.role.entity.SysRole;

public interface SysRoleService {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRole record);

	SysPermission selectByName(String roleName);
}