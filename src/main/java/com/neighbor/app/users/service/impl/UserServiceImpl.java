package com.neighbor.app.users.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.neighbor.app.users.constants.UserContainer;
import com.neighbor.app.users.dao.UserInfoMapper;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;

@Service
public class UserServiceImpl implements UserService{

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserContainer userContainer;
    @Autowired
    private UserInfoMapper userInfoMapper;
	@Autowired
	private UserWalletService userWalletService; 
	
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

	@Override
	public Long selectPageTotalCount(UserInfo userInfo) {
		return userInfoMapper.selectPageTotalCount(userInfo);
	}

	@Override
	public List<UserInfo> selectPageByObjectForList(UserInfo userInfo) {
		return userInfoMapper.selectPageByObjectForList(userInfo);
	}
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

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public UserInfo createUser(UserInfo record) { 
		insertSelective(record);
		record.setNickName(record.getId()+"");
		record.setUserAccount(record.getId()+"");
		updateByPrimaryKeySelective(record);
		
		UserWallet wallet = new UserWallet();
		wallet.setuId(record.getId());
		
		userWalletService.insertSelective(wallet);
		logger.debug("user============:{}",record);
		return record;
	}

}