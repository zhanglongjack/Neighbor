package com.neighbor.app.friend.service;

import com.neighbor.app.friend.entity.Friend;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.ResponseResult;

public interface FriendService {
   public ResponseResult listRecord(UserInfo user, Friend friend) throws Exception;
}
