package com.neighbor.security.role.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.security.role.entity.SysUserRoleKey;

public interface SysUserRoleService {
    int deleteByPrimaryKey(SysUserRoleKey key);

    int insert(SysUserRoleKey record);

	List<SysUserRoleKey> listByUserId(Long getuId);
}