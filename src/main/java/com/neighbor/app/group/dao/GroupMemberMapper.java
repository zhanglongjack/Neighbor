package com.neighbor.app.group.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.app.group.entity.GroupMember;

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

	List<GroupMember> selectBySelective(GroupMember groupMember);
}