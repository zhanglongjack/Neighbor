package com.neighbor.security.role.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.neighbor.security.role.entity.SysUserRoleKey;

@Mapper
public interface SysUserRoleMapper {
    int deleteByPrimaryKey(SysUserRoleKey key);

    int insert(SysUserRoleKey record);

    @Select("select * from sys_user_role where user_id = #{userId}")
	List<SysUserRoleKey> listByUserId(Long userId);
}