package com.neighbor.app.users.service;

import java.math.BigDecimal;
import java.util.List;

import com.neighbor.app.robot.entity.RobotConfig;
import com.neighbor.app.users.entity.UserConfig;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.common.util.ResponseResult;

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

    UserInfo builderUserInfo(UserInfo record) throws Exception;

    Long selectPageTotalCount(UserInfo queryUser);

    List<UserInfo> selectPageByObjectForList(UserInfo queryUser);

	UserInfo buildRobotInfo(UserInfo record, RobotConfig robot, UserWallet wallet);

	void updateRobotInfo(UserInfo user, RobotConfig robot, UserWallet wallet);

	UserInfo selectByRobotId(Long robotId);

    UserInfo selectByReCode(String reCode);

    void updateRobotWallet(Long userId, String action, BigDecimal amount);

    ResponseResult changePhone(String phone, String verfiyCode, UserInfo user);
}