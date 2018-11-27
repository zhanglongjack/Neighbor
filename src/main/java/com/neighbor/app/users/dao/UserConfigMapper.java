package com.neighbor.app.users.dao;

import com.neighbor.app.users.entity.UserConfig;

public interface UserConfigMapper {
    int deleteByPrimaryKey(Long userId);

    int insertSelective(UserConfig record);

    UserConfig selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(UserConfig record);

}