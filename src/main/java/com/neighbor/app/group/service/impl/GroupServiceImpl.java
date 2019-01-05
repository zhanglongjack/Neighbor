package com.neighbor.app.group.service.impl;

import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.group.dao.GroupMapper;
import com.neighbor.app.group.dao.GroupMemberMapper;
import com.neighbor.app.group.entity.Group;
import com.neighbor.app.group.entity.GroupMember;
import com.neighbor.app.group.service.GroupService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupMemberMapper groupMemberMapper;

    @Override
    public ResponseResult chatlist(UserInfo user, Group group) throws Exception {
        ResponseResult result = new ResponseResult();
        group.setUserId(user.getId());
        Long total = groupMapper.selectPageTotalCount(group);
        List<Group> pageList = groupMapper.selectPageByObjectForList(group);
        PageTools pageTools = group.getPageTools();
        pageTools.setTotal(total);
        result.addBody("resultList", pageList);
        result.addBody("pageTools", pageTools);
        return result;
    }

    @Override
    public ResponseResult chatInfo(UserInfo user, GroupMember member) throws Exception {
        ResponseResult result = new ResponseResult();
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupId(member.getGroupId());
        groupMember.setUserId(user.getId());
        groupMember = groupMemberMapper.selectGroupMember(groupMember);
        if(groupMember==null){
            result.setErrorCode(ErrorCodeDesc.failed.getValue());
            result.setErrorMessage("用户不在群内！");
            return result;
        }
        GroupMember groupMember1 = new GroupMember();
        groupMember1.setPageTools(member.getPageTools());
        groupMember1.setGroupId(member.getGroupId());
        groupMember1.setFriendUserId(user.getId());
        Long total = groupMemberMapper.selectPageTotalCount(groupMember1);
        List<GroupMember> pageList = groupMemberMapper.selectPageByObjectForList(groupMember1);
        PageTools pageTools = member.getPageTools();
        pageTools.setTotal(total);
        result.addBody("resultList", pageList);
        result.addBody("pageTools", pageTools);
        result.addBody("groupMember", groupMember);
        return result;
    }

    @Override
    public ResponseResult memberList(UserInfo user, GroupMember member) throws Exception {
        ResponseResult result = new ResponseResult();
        GroupMember groupMember1 = new GroupMember();
        groupMember1.setPageTools(member.getPageTools());
        groupMember1.setGroupId(member.getGroupId());
        groupMember1.setFriendUserId(user.getId());
        Long total = groupMemberMapper.selectPageTotalCount(groupMember1);
        List<GroupMember> pageList = groupMemberMapper.selectPageByObjectForList(groupMember1);
        PageTools pageTools = member.getPageTools();
        pageTools.setTotal(total);
        result.addBody("resultList", pageList);
        result.addBody("pageTools", pageTools);
        return result;
    }

    @Override
    public ResponseResult chatSetting(GroupMember groupMember) throws Exception {
        ResponseResult result = new ResponseResult();
        groupMemberMapper.updateByPrimaryKeySelective(groupMember);
        return result;
    }

    @Override
    public ResponseResult clearChatHistory(UserInfo user, GroupMember groupMember) throws Exception {
        ResponseResult result = new ResponseResult();
        //TODO 清除群聊记录
        return result;
    }

    @Override
    public ResponseResult setting(Group group) throws Exception {
        ResponseResult result = new ResponseResult();
        groupMapper.updateByPrimaryKeySelective(group);
        return result;
    }

    @Override
    public ResponseResult viewinfo(Group group) throws Exception {
        ResponseResult result = new ResponseResult();
        result.addBody("group",groupMapper.selectByPrimaryKey(group.getId()));
        return result;
    }
    
    @Override
    public List<Group> selectBySelective(Group group) throws Exception {
    	return groupMapper.selectBySelective(group);
    }

	@Override
	public List<GroupMember> selectUserOwnGroupsBy(Long userId) {
		GroupMember groupMember = new GroupMember();
		groupMember.setUserId(userId);
		return groupMemberMapper.selectBySelective(groupMember);
	}
	
	@Override
	public GroupMember selectGroupMemberBy(Long userId,Long groupId) {
		GroupMember groupMember = new GroupMember();
		groupMember.setUserId(userId);
		groupMember.setGroupId(groupId);
		return groupMemberMapper.selectGroupMember(groupMember);
	}
	
	private void addGroupMember(GroupMember member){
		groupMemberMapper.insertSelective(member);
	}
	
	@Override
	public void addGroupMember(Long groupId,Long userId,String memberType){
		GroupMember member = new GroupMember();
		member.setUserId(userId);
		member.setGroupId(groupId);
		member.setMemberType(memberType);
		addGroupMember(member);
	}
	
	@Override
	public void addGroupMembers(Long groupId,List<Long> userList){
		for(Long userId : userList){
			addGroupMember(groupId, userId,null);
		}
	}

    @Override
    public ResponseResult exitGroup(UserInfo user, GroupMember groupMember) throws Exception {
        ResponseResult result = new ResponseResult();
        groupMemberMapper.deleteByPrimaryKey(groupMember.getMemberId());
        return result;
    }

}
