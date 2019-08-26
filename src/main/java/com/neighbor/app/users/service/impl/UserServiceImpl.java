package com.neighbor.app.users.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.common.exception.ParamsCheckException;
import com.neighbor.common.sms.TencentSms;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.neighbor.app.common.util.RandomUtil;
import com.neighbor.app.robot.entity.RobotConfig;
import com.neighbor.app.robot.service.RobotConfigService;
import com.neighbor.app.users.constants.UserContainer;
import com.neighbor.app.users.dao.UserConfigMapper;
import com.neighbor.app.users.dao.UserInfoMapper;
import com.neighbor.app.users.entity.UserConfig;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;

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

	@Autowired
	private RobotConfigService robotConfigService;

    @Override
    public int deleteByPrimaryKey(Long uId) {
        return userInfoMapper.deleteByPrimaryKey(uId);
    }

    @Override
    public int insertSelective(UserInfo record) {
        String reCode = null;
//        boolean b = true;
        for(int idx = 10;idx<=10;idx--){
            reCode = RandomUtil.getReCode();
            UserInfo userInfo = selectByReCode(reCode);
            if(userInfo == null){
//                b = false;
                break;
            }
        }
        record.setNickName(RandomUtil.getReCode());
        record.setReCode(reCode);
        int count = userInfoMapper.insertSelective(record);
        userContainer.buildUserInfo();
        return count;
    }

    public int insertUserConfigSelective(UserConfig record) {
        record.setHaveShock(UserConfig.HaveShockDesc.haveShock.getValue());
        record.setHaveVoice(UserConfig.HaveVoiceDesc.haveVoice.getValue());
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
    public UserInfo builderUserInfo(UserInfo record) throws Exception{
        logger.info("创建用户信息:" + record);

        insertSelective(record);

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

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserInfo buildRobotInfo(UserInfo record,RobotConfig robot,UserWallet wallet) {
    	logger.info("创建机器人配置信息:" + robot);
    	robotConfigService.insertSelective(robot);

    	record.setRobotSno(robot.getRobotId()+"");
    	record.setUpUserId(1L);
    	logger.info("创建用户信息:" + record);
    	insertSelective(record);

//    	record.setNickName(record.getNickName()==null?record.getId() + "":record.getNickName());
//    	record.setUserAccount(record.getUserAccount()==null?record.getId() + "":record.getUserAccount());
    	updateByPrimaryKeySelective(record);

        logger.info("创建用户设置信息:" + record);
        UserConfig config = new UserConfig();
        config.setNoPasswordPay("0");
        insertUserConfigSelective(config);

    	logger.info("创建钱包信息:" + record);
    	wallet.setuId(record.getId());
    	userWalletService.insertSelective(wallet);

    	return record;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateRobotInfo(UserInfo user, RobotConfig robot, UserWallet wallet) {
		updateByPrimaryKeySelective(user);
		robotConfigService.updateByPrimaryKeySelective(robot);
		
//		UserWallet walletResult = userWalletService.selectByPrimaryUserId(user.getId());
//		wallet.setId(walletResult.getId());
//		userWalletService.updateByPrimaryKeySelective(wallet);
	}

    @Override
    public void updateRobotWallet(Long userId, String action, BigDecimal amount) {
        userWalletService.updateRobotWallet(userId,action,amount);
    }

    @Override
	public UserInfo selectByRobotId(Long robotId) {
		return userInfoMapper.selectByRobotId(robotId);
	}

    @Override
    public UserInfo selectByReCode(String reCode) {
        return userInfoMapper.selectByReCode(reCode);
    }

    @Override
    public ResponseResult changePhone(String phone, String verfiyCode, UserInfo user){

        ResponseResult result = new ResponseResult();
        result.setErrorCode(ErrorCodeDesc.failed.getValue());

        if(user.getMobilePhone().equals(phone)){
            result.setErrorMessage("新手机号码与旧手机相同！无需修改");
            return result;
        }

        //判断验证码
        boolean isValid = verfiyCode.equals(TencentSms.smsCache.get(phone));
        if (!isValid) {
            result.setErrorMessage("验证码错误!");
            return result;
        }

        //查询相同的手机号码
         UserInfo userInfo = selectByUserPhone(phone);
        if(userInfo!=null){
            result.setErrorMessage("手机号码："+phone+"已被占用,请更换其他手机号!");
            return result;
        }

        UserInfo update = new UserInfo();
        update.setUserID(user.getUserID());
        update.setMobilePhone(phone);
        updateByPrimaryKeySelective(update);
        result.setErrorCode(ErrorCodeDesc.success.getValue());

        TencentSms.smsCache.remove(phone);

        return result;
    }
}