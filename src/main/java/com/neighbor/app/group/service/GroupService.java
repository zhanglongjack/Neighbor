package com.neighbor.app.group.service;

import com.neighbor.app.group.entity.Group;
import com.neighbor.app.group.entity.GroupMember;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.ResponseResult;

public interface GroupService {
    ResponseResult chatlist(UserInfo user, Group group) throws Exception;

    ResponseResult chatInfo(UserInfo user, GroupMember groupMember) throws Exception;

    ResponseResult memberList(UserInfo user, GroupMember groupMember) throws Exception;

    ResponseResult chatSetting(GroupMember groupMember) throws Exception;

    ResponseResult clearChatHistory(UserInfo user, GroupMember groupMember) throws Exception;

    ResponseResult setting(Group group) throws Exception;

    ResponseResult viewinfo(Group group) throws Exception;
}
