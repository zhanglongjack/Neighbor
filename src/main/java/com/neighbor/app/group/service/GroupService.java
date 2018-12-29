package com.neighbor.app.group.service;

import com.neighbor.app.group.entity.Group;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.ResponseResult;

public interface GroupService {
    ResponseResult chatlist(UserInfo user, Group group) throws Exception;
}
