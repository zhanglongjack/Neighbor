package com.neighbor.app.group.service.impl;

import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.group.constants.ApplyStatesDesc;
import com.neighbor.app.group.dao.GroupApplyMapper;
import com.neighbor.app.group.dao.GroupMapper;
import com.neighbor.app.group.dao.GroupMemberMapper;
import com.neighbor.app.group.entity.Group;
import com.neighbor.app.group.entity.GroupApply;
import com.neighbor.app.group.entity.GroupMember;
import com.neighbor.app.group.service.GroupService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.DateUtils;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupMemberMapper groupMemberMapper;

    @Autowired
    private GroupApplyMapper groupApplyMapper;

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
        groupMember1.setUserId(member.getUserId());
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
        GroupMember member = groupMemberMapper.selectByPrimaryKey(groupMember.getMemberId());
        GroupApply groupApply = new GroupApply();
        groupApply.setGroupId(member.getGroupId());
        groupApply.setEnterUserId(member.getUserId());
        groupApplyMapper.deleteByGroupApply(groupApply);
        groupMemberMapper.deleteByPrimaryKey(groupMember.getMemberId());
        return result;
    }

    @Override
    public ResponseResult enterGroup(UserInfo user, GroupMember groupMember) throws Exception {
        ResponseResult result = new ResponseResult();
        String[] userIds = groupMember.getFriendUserIds().split(",");
        Date date = new Date();
        Group group = groupMapper.selectByPrimaryKey(groupMember.getGroupId());
        if(group==null){
            result.setErrorCode(ErrorCodeDesc.failed.getValue());
            result.setErrorMessage("群不存在！");
            return result;
        }
        if(checkMember(groupMember.getGroupId(),user.getId(),result)){
            //群主
            if(userIds.length>0){
                for(int i=0;i<userIds.length;i++){
                    GroupMember temp =new GroupMember();
                    temp.setGroupId(groupMember.getGroupId());
                    temp.setUserId(Long.valueOf(userIds[i]));
                    temp.setCreateTime(date);
                    groupMemberMapper.insertSelective(temp);
                }
            }
            result.addBody("memberType","1");//群主
            result.addBody("group",group);//群主
        }else{
           //非群主
            if(userIds.length>0){
                for(int i=0;i<userIds.length;i++){
                    GroupApply temp =new GroupApply();
                    temp.setGroupId(groupMember.getGroupId());
                    temp.setEnterUserId(Long.valueOf(userIds[i]));
                    Long total = groupApplyMapper.selectPageTotalCount(temp);
                    if(total>0)continue;
                    temp.setInviteUserId(user.getId());
                    temp.setShowFlag("0");
                    temp.setStates(ApplyStatesDesc.apply.getValue()+"");
                    temp.setCreateTime(date);
                    groupApplyMapper.insertSelective(temp);
                }
            }
            result.addBody("memberType","0");//非群主
        }

        return result;
    }

    @Override
    public ResponseResult enterGroupApplyNum(UserInfo user, GroupApply groupApply) {
        ResponseResult result = new ResponseResult();
        groupApply.setShowFlag("0");
        Long total = groupApplyMapper.selectPageTotalCount(groupApply);
        result.addBody("total",total);
        return result;
    }

    @Override
    public ResponseResult clearGroupApplyNum(UserInfo user, GroupApply groupApply) {
        ResponseResult result = new ResponseResult();
        groupApplyMapper.clearGroupApplyNum(groupApply);
        return result;
    }

    private boolean checkMember(Long groupId,Long userId,ResponseResult result){
        //检查是否去群主
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupId(groupId);
        groupMember.setUserId(userId);
        groupMember.setMemberType("1"); //群主
        Long total = groupMemberMapper.selectPageTotalCount(groupMember);
        if(total==0){
            result.setErrorCode(ErrorCodeDesc.failed.getValue());
            result.setErrorMessage("非群主不能添加");
            return false;
        }
        return true;
    }

    @Override
    public ResponseResult groupApply(UserInfo user, GroupApply groupApply) throws Exception {
        ResponseResult result = new ResponseResult();
        //检查是否去群主
        if(!checkMember(groupApply.getGroupId(),user.getId(),result)){
            return result;
        }
        GroupApply apply = groupApplyMapper.selectByPrimaryKey(groupApply.getRecordId());
        if(apply==null){
            result.setErrorCode(ErrorCodeDesc.failed.getValue());
            result.setErrorMessage("记录不存在~");
            return result;
        }
        if(!(ApplyStatesDesc.apply.getValue()+"").equals(apply.getStates())){
            result.setErrorCode(ErrorCodeDesc.failed.getValue());
            result.setErrorMessage("记录已经审核过了~");
            return result;
        }
        Date date = new Date();
        GroupApply update = new GroupApply();
        update.setId(apply.getId());
        update.setUpdateTime(date);
        //审核通过
        if(groupApply.getStates().equals(ApplyStatesDesc.pass.getValue()+"")){
            GroupMember member = new GroupMember();
            member.setGroupId(groupApply.getGroupId());
            member.setUserId(apply.getEnterUserId());
            member.setCreateTime(date);
            member.setMemberType("0");
            groupMemberMapper.insertSelective(member);
            update.setStates(ApplyStatesDesc.pass.getValue()+"");
            Group group = groupMapper.selectByPrimaryKey(groupApply.getGroupId());
            result.addBody("group",group);
        }else{
            update.setStates(ApplyStatesDesc.reject.getValue()+"");
        }
        groupApplyMapper.updateByPrimaryKeySelective(update);
        return result;
    }

    @Override
    public ResponseResult groupApplyRecord(UserInfo user, GroupApply member) throws Exception {
        ResponseResult result = new ResponseResult();
        GroupApply groupApply = new GroupApply();
        groupApply.setPageTools(member.getPageTools());
        groupApply.setGroupId(member.getGroupId());
        Long total = groupApplyMapper.selectPageTotalCount(groupApply);
        List<GroupApply> pageList = groupApplyMapper.selectFullInfoPageForList(groupApply);
        PageTools pageTools = member.getPageTools();
        pageTools.setTotal(total);
        result.addBody("resultList", pageList);
        result.addBody("pageTools", pageTools);
        return result;
    }

    @Override
    public Long selectPageTotalCount(Group group) {
        return groupMapper.selectPageTotalCount(group);
    }

    @Override
    public List<Group> selectPageByObjectForList(Group group) {
        return  groupMapper.selectPageByObjectForList(group);
    }

    @Override
    public void createGroup(Group group) {
        Date date = new Date();
        group.setCreateTime(date);
        group.setCreDate(DateUtils.getStringDateShort());
        group.setCreTime(DateUtils.getTimeShort());
        if(group.getGameId()==null){
            group.setGameId(1L);
        }
        group.setStates("1");
        groupMapper.insertSelective(group);
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupId(group.getId());
        groupMember.setMemberType("1");//群主
        groupMember.setUserId(group.getGroupOwnerUserId());
        groupMember.setCreateTime(date);
        groupMemberMapper.insertSelective(groupMember);


    }

    @Override
    public void updateGroup(Group group) {
        Group queryGroup = new Group();
        queryGroup.setId(group.getGroupId());
        queryGroup.setPageTools(new PageTools());
        List<Group> list = selectPageByObjectForList(queryGroup);
        Group temp = list.get(0);
        Date date = new Date();
        group.setUpdateTime(date);
        groupMapper.updateByPrimaryKeySelective(group);
        if(temp.getGroupOwnerUserId().longValue()!=group.getGroupOwnerUserId().longValue()){
            GroupMember queryMember = new GroupMember();
            queryMember.setUserId(group.getGroupOwnerUserId());
            queryMember.setGroupId(group.getGroupId());
            GroupMember updateMember = groupMemberMapper.selectGroupMember(queryMember);
            queryMember.setUserId(temp.getGroupOwnerUserId());
            GroupMember tempMember = groupMemberMapper.selectGroupMember(queryMember);
            tempMember.setMemberType("0");//非群主
            tempMember.setUpdateTime(date);
            groupMemberMapper.updateByPrimaryKeySelective(tempMember);
            if(updateMember!=null){
                updateMember.setMemberType("1");//群主
                updateMember.setUpdateTime(date);
                groupMemberMapper.updateByPrimaryKeySelective(updateMember);
            }else{
                GroupMember groupMember = new GroupMember();
                groupMember.setGroupId(group.getGroupId());
                groupMember.setMemberType("1");//群主
                groupMember.setUserId(group.getGroupOwnerUserId());
                groupMember.setCreateTime(date);
                groupMemberMapper.insertSelective(groupMember);
            }
        }
    }

	@Override
	public List<GroupMember> selectRobotGroupMemberBy(Long groupId) {
		return groupMemberMapper.selectRobotGroupMemberBy(groupId);
	}

	@Override
	public Group selectByPrimeryId(Long groupId) {
		return groupMapper.selectByPrimaryKey(groupId);
	}
}
