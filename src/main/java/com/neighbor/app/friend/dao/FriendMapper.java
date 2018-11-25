package com.neighbor.app.friend.dao;

import com.neighbor.app.friend.entity.Friend;
import com.neighbor.app.friend.entity.FriendApply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface FriendMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(Friend record);

    Friend selectByPrimaryKey(Long id);

    Friend selectByMap(Friend friend);

    int updateByPrimaryKeySelective(Friend record);

    Long selectPageTotalCount(Friend record);

    List<Friend> selectPageByObjectForList(Friend record);

    List<Friend> selectAll();
    List<Friend> selectFullInfoPageForList(Friend record);

}