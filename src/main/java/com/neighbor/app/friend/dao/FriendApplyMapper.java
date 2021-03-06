package com.neighbor.app.friend.dao;

import com.neighbor.app.friend.entity.Friend;
import com.neighbor.app.friend.entity.FriendApply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FriendApplyMapper {

    int deleteByPrimaryKey(Long id);

    int insertFriendApply(FriendApply friendApply);

    Long selectPageTotalCount(FriendApply record);

    List<FriendApply> selectFullInfoPageForList(FriendApply record);

    FriendApply selectByMap(FriendApply friend);

    int updateByPrimaryKeySelective(FriendApply record);

}