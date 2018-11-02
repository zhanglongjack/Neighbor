package com.neighbor.app.users.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neighbor.app.users.constants.UserContainer;
import com.neighbor.app.users.dao.UserInfoMapper;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserContainer userContainer;
    @Autowired
    private UserInfoMapper userInfoMapper;

	@Override
	public int deleteByPrimaryKey(Long uId) {
		return userInfoMapper.deleteByPrimaryKey(uId);
	}

	@Override
	public int insertSelective(UserInfo record) {
		int count = userInfoMapper.insertSelective(record);
		userContainer.buildUserInfo();
		return count;
	}

	@Override
	public UserInfo selectByPrimaryKey(Long uId) {
		return userInfoMapper.selectByPrimaryKey(uId);
	}

	@Override
	public int updateByPrimaryKeySelective(UserInfo record) {
		int count =  userInfoMapper.updateByPrimaryKeySelective(record);
		userContainer.buildUserInfo();
		return count;
	}

	@Override
	public UserInfo selectByUserPhone(String phone) {
		return userInfoMapper.selectByUserPhone(phone);
	}
//
//	@Override
//	public Long selectPageTotalCount(UserInfo userInfo) {
//		return userInfoMapper.selectPageTotalCount(userInfo);
//	}
//
//	@Override
//	public List<UserInfo> selectPageByObjectForList(UserInfo userInfo) {
//		return userInfoMapper.selectPageByObjectForList(userInfo);
//	}
//
//	@Override
//	public List<UserInfo> selectAllForMap() {
//		return userInfoMapper.selectAll();
//	}
//
//	@Override
//	public UserInfo selectByName(String username) {
//		return userInfoMapper.selectByName(username);
//	}

}