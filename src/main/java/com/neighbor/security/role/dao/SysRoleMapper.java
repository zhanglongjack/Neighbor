package com.neighbor.security.role.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.neighbor.security.permission.entity.SysPermission;
import com.neighbor.security.role.entity.SysRole;

@Mapper
public interface SysRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRole record);

    @Select("select * from sys_role where name = #{name}")
	SysPermission selectByName(String name);

}