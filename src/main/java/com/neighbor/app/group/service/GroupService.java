package com.neighbor.app.group.service;

import java.util.List;

import com.neighbor.app.group.entity.Group;
import com.neighbor.app.group.entity.GroupApply;
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

	List<Group> selectBySelective(Group group) throws Exception;

	List<GroupMember> selectUserOwnGroupsBy(Long userId);

	GroupMember selectGroupMemberBy(Long userId, Long groupId);

	void addGroupMember(Long groupId, Long userId, String memberType);

	void addGroupMembers(Long groupId, List<Long> userList);

    ResponseResult exitGroup(UserInfo user, GroupMember groupMember) throws Exception;

    ResponseResult enterGroup(UserInfo user, GroupMember groupMember) throws Exception;

    ResponseResult enterGroupApplyNum(UserInfo user, GroupApply groupMember);

    ResponseResult groupApplyRecord(UserInfo user, GroupApply groupMember)  throws Exception;

    ResponseResult clearGroupApplyNum(UserInfo user, GroupApply groupApply);

    ResponseResult groupApply(UserInfo user, GroupApply groupApply) throws Exception;

    Long selectPageTotalCount(Group group);

    List<Group> selectPageByObjectForList(Group group);


    void createGroup(Group group);

    void updateGroup(Group group);

	List<GroupMember> selectRobotGroupMemberBy(GroupMember member);

	Group selectByPrimeryId(Long groupId);

	List<Group> selectPageByRobotGroupRelation(Group queryObject);

	Long selectPageTotalCountByRobotGroupRelation(Group queryObject);

}
