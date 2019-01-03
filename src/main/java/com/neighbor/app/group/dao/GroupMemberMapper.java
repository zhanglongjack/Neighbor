package com.neighbor.app.group.dao;

import com.neighbor.app.group.entity.GroupMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupMemberMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(GroupMember record);

    GroupMember selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GroupMember record);


    Long selectPageTotalCount(GroupMember record);

    List<GroupMember> selectPageByObjectForList(GroupMember record);

    List<GroupMember> selectAll();

    GroupMember selectGroupMember(GroupMember record);
}