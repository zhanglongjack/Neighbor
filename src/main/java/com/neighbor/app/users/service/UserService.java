package com.neighbor.app.users.service;

import java.util.List;

import com.neighbor.app.users.entity.UserInfo;

public interface UserService {

	int deleteByPrimaryKey(Long uId);

	int insertSelective(UserInfo record);

	UserInfo selectByPrimaryKey(Long uId);

	int updateByPrimaryKeySelective(UserInfo record);

	UserInfo selectByUserPhone(String phone);
//
//	Long selectPageTotalCount(UserInfo userInfo);
//
//	List<UserInfo> selectPageByObjectForList(UserInfo userInfo);
//
//	List<UserInfo> selectAllForMap();
//
//	UserInfo selectByName(String username);

	UserInfo createUser(UserInfo record);

}