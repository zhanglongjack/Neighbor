package com.neighbor.app.users.service;

import java.util.List;

import com.neighbor.app.users.entity.UserConfig;
import com.neighbor.app.users.entity.UserInfo;

public interface UserService {

    int deleteByPrimaryKey(Long uId);

    int insertSelective(UserInfo record);

    public int insertUserConfigSelective(UserConfig record);

    public UserConfig selectUserConfigByPrimaryKey(Long uId);

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

    UserInfo builderUserInfo(UserInfo record);

    Long selectPageTotalCount(UserInfo queryUser);

    List<UserInfo> selectPageByObjectForList(UserInfo queryUser);

}