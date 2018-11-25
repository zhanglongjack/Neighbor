package com.neighbor.app.group.dao;

import com.neighbor.app.group.entity.Group;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupMapper {
    int deleteByPrimaryKey(Long id);
    int insertSelective(Group record);
    Group selectByPrimaryKey(Long id);
    int updateByPrimaryKeySelective(Group record);

    Long selectPageTotalCount(Group record);

    List<Group> selectPageByObjectForList(Group record);

    List<Group> selectAll();

}