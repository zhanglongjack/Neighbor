package com.neighbor.app.users.service;

import java.util.List;

import com.neighbor.app.robot.entity.RobotConfig;
import com.neighbor.app.users.entity.UserConfig;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.wallet.entity.UserWallet;

public interface UserService {

    int deleteByPrimaryKey(Long uId);

    int insertSelective(UserInfo record);

    public int insertUserConfigSelective(UserConfig record);

    public void userConfigSetting(UserConfig userConfig);

    public UserConfig selectUserConfigByPrimaryKey(Long uId);

    UserInfo selectByPrimaryKey(Long uId);

    int updateByPrimaryKeySelective(UserInfo record);

    public int userPasswordEdit(UserInfo record);

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

	UserInfo buildRobotInfo(UserInfo record, RobotConfig robot, UserWallet wallet);

	void updateRobotInfo(UserInfo user, RobotConfig robot, UserWallet wallet);

	UserInfo selectByRobotId(Long robotId);

}