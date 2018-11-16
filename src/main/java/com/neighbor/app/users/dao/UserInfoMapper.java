package com.neighbor.app.users.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.app.users.entity.UserInfo;

@Mapper
public interface UserInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserInfo record);

	UserInfo selectByUserPhone(String phone);

	Long selectPageTotalCount(UserInfo userInfo);

	List<UserInfo> selectPageByObjectForList(UserInfo userInfo);
    
}