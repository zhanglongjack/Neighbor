package com.neighbor.app.sys.dao;

import com.neighbor.app.sys.entity.SysVersion;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysVersionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysVersion record);

    int insertSelective(SysVersion record);

    SysVersion selectByPrimaryKey(Long id);

    SysVersion selectLast();

    int updateByPrimaryKeySelective(SysVersion record);

    int updateByPrimaryKey(SysVersion record);
}