package com.neighbor.app.users.service.impl;

import com.neighbor.app.users.constants.UserContainer;
import com.neighbor.app.users.dao.UserConfigMapper;
import com.neighbor.app.users.dao.UserInfoMapper;
import com.neighbor.app.users.entity.UserConfig;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserContainer userContainer;
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserConfigMapper userConfigMapper;

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

    public int insertUserConfigSelective(UserConfig record) {
        int count = userConfigMapper.insertSelective(record);
        return count;
    }

    public void userConfigSetting(UserConfig userConfig){
        userConfigMapper.updateByPrimaryKeySelective(userConfig);
    }

    public UserConfig selectUserConfigByPrimaryKey(Long uId) {
        return userConfigMapper.selectByPrimaryKey(uId);
    }

    @Override
    public UserInfo selectByPrimaryKey(Long uId) {
        return userInfoMapper.selectByPrimaryKey(uId);
    }

    @Override
    public int updateByPrimaryKeySelective(UserInfo record) {
        int count = userInfoMapper.updateByPrimaryKeySelective(record);
        userContainer.buildUserInfo();
        return count;
    }

    public int userPasswordEdit(UserInfo record) {
        return userInfoMapper.userPasswordEdit(record);
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserInfo builderUserInfo(UserInfo record) {
        logger.info("创建用户信息:" + record);
        insertSelective(record);
        record.setNickName(record.getId() + "");
        record.setUserAccount(record.getId() + "");
        updateByPrimaryKeySelective(record);

        logger.info("创建用户设置信息:" + record);
        UserConfig config = new UserConfig();
        config.setNoPasswordPay("0");
        insertUserConfigSelective(config);

        logger.info("创建钱包信息:" + record);
        UserWallet wallet = new UserWallet();
        wallet.setuId(record.getId());
        userWalletService.insertSelective(wallet);

        return record;
    }

}