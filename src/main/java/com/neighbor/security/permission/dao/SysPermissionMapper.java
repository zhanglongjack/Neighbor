package com.neighbor.security.permission.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.neighbor.security.permission.entity.SysPermission;

@Mapper
public interface SysPermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysPermission record);

    @Select("select * from sys_permission where role_id=#{roleId}")
	List<SysPermission> listByRoleId(Integer roleId);

}