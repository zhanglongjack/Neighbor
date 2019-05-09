package com.neighbor.app.friend.service;

import com.neighbor.app.friend.entity.Friend;
import com.neighbor.app.friend.entity.FriendApply;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.ResponseResult;

public interface FriendService {
   public ResponseResult listRecord(UserInfo user, Friend friend) throws Exception;

   public Friend viewFriendByUserIdAndFriendId(Friend record)throws Exception;

   public FriendApply viewFriendApplyByUserIdAndFriendId(FriendApply friendApply) throws Exception;

   public void insertFriend(Friend friend) throws Exception;

   public void insertFriendApply(FriendApply friendApply)throws Exception;

   public void deleteFriendApplyByPrimaryKey(Long id) throws Exception;

   public void acceptFriend(FriendApply friendApply) throws Exception;

   public void deleteFriend(Friend friend) throws Exception;

   public ResponseResult listFriendApplyRecord(UserInfo user, FriendApply friendApply) throws Exception;

   public void contactFriend(Long userId,Long friendUserId) throws Exception;

}
