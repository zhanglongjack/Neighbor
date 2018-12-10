package com.neighbor.app.friend.service.impl;

import com.neighbor.app.chatlist.entity.ChatList;
import com.neighbor.app.chatlist.service.ChatListService;
import com.neighbor.app.friend.constants.FriendStatesDesc;
import com.neighbor.app.friend.dao.FriendApplyMapper;
import com.neighbor.app.friend.dao.FriendMapper;
import com.neighbor.app.friend.entity.Friend;
import com.neighbor.app.friend.entity.FriendApply;
import com.neighbor.app.friend.service.FriendService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;
import com.neighbor.common.util.DateFormateType;
import com.neighbor.common.util.DateUtils;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {
    private static final Logger logger = LoggerFactory.getLogger(FriendServiceImpl.class);

    @Autowired
    private FriendMapper friendMapper;

    @Autowired
    private FriendApplyMapper friendApplyMapper;

    @Autowired
    private ChatListService chatListService;

    @Autowired
    private UserService userService;


    @Override
    public ResponseResult listRecord(UserInfo user, Friend friend) throws Exception {
        logger.info("好友列表...");
        ResponseResult result = new ResponseResult();
        friend.setUserId(user.getId());
        Long total = friendMapper.selectPageTotalCount(friend);
        List<Friend> pageList = friendMapper.selectFullInfoPageForList(friend);
        PageTools pageTools = friend.getPageTools();
        pageTools.setTotal(total);
        result.addBody("resultList", pageList);
        result.addBody("pageTools", pageTools);
        return result;
    }

    @Override
    public ResponseResult listFriendApplyRecord(UserInfo user, FriendApply friendApply) throws Exception {
        logger.info("好友列表...");
        ResponseResult result = new ResponseResult();
        friendApply.setFriendUserId(user.getId());
        Long total = friendApplyMapper.selectPageTotalCount(friendApply);
        List<FriendApply> pageList = friendApplyMapper.selectFullInfoPageForList(friendApply);
        PageTools pageTools = friendApply.getPageTools();
        pageTools.setTotal(total);
        result.addBody("resultList", pageList);
        result.addBody("pageTools", pageTools);
        return result;
    }

    public Friend viewFriendByUserIdAndFriendId(Friend friend) throws Exception {
        return friendMapper.selectByMap(friend);
    }

    public FriendApply viewFriendApplyByUserIdAndFriendId(FriendApply friendApply) throws Exception {
        return friendApplyMapper.selectByMap(friendApply);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertFriendApply(FriendApply friendApply) throws Exception {
        friendApplyMapper.insertFriendApply(friendApply);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertFriend(Friend friend) throws Exception {
        friendMapper.insertSelective(friend);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteFriend(Friend friend) throws Exception {
        //删除自己的好友关系
        Friend friendA = friendMapper.selectByMap(friend);
        friendMapper.deleteByPrimaryKey(friendA.getId());
        //对方好更新删除标记
        Long userId = friend.getUserId();
        Long friendUserId = friend.getFriendUserId();
        friend.setUserId(friendUserId);
        friend.setFriendUserId(userId);
        Friend friendB = friendMapper.selectByMap(friend);
        Friend updateFriend = new Friend();
        updateFriend.setStates(FriendStatesDesc.delete.getDes());
        updateFriend.setId(friendB.getId());
        friendMapper.updateByPrimaryKeySelective(updateFriend);

        UserInfo user = userService.selectByPrimaryKey(userId);
        ChatList chatList = new ChatList();
        chatList.setFriendId(friendUserId);
        chatListService.delChat(user, chatList);

    }

    public void deleteFriendApplyByPrimaryKey(Long id) throws Exception {
        friendApplyMapper.deleteByPrimaryKey(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void acceptFriend(FriendApply friendApply) throws Exception {

        Date currentTime = new Date();
        friendApply.setStates(FriendApply.StatesDesc.pass.getValue());
        friendApply.setUpdateTime(currentTime);
        friendApplyMapper.updateByPrimaryKeySelective(friendApply);

        Long userId = friendApply.getUserId();
        Long friendUserId = friendApply.getFriendUserId();

        Friend friend = new Friend();
        friend.setUserId(userId);
        friend.setFriendUserId(friendUserId);
        Friend friendOld = friendMapper.selectByMap(friend);
        if (friendOld == null) {
            Friend friendActive = new Friend();
            friendActive.setCreateTime(currentTime);
            friendActive.setUpdateTime(currentTime);
            friendActive.setContactDate(DateUtils.formatDateStr(currentTime, DateFormateType.SHORT_FORMAT));
            friendActive.setContactTime(DateUtils.formatDateStr(currentTime, DateFormateType.SHORT_FORMAT));
            friendActive.setUserId(userId);
            friendActive.setFriendUserId(friendUserId);
            friendActive.setStates(FriendStatesDesc.normal.getDes());
            friendActive.setAddDirection(FriendApply.AddDirectionDesc.activeAdd.getValue());
            friendActive.setAddType(FriendApply.AddTypeDesc.appAdd.getValue());
            friendActive.setCode(String.valueOf(userId));
            insertFriend(friendActive);

            Friend friendAccept = new Friend();
            friendAccept.setCreateTime(currentTime);
            friendAccept.setUpdateTime(currentTime);
            friendAccept.setContactDate(DateUtils.formatDateStr(currentTime, DateFormateType.SHORT_FORMAT));
            friendAccept.setContactTime(DateUtils.formatDateStr(currentTime, DateFormateType.SHORT_FORMAT));
            friendAccept.setUserId(friendUserId);
            friendAccept.setFriendUserId(userId);
            friendAccept.setStates(FriendStatesDesc.normal.getDes());
            friendAccept.setAddDirection(FriendApply.AddDirectionDesc.acceptAdd.getValue());
            friendAccept.setAddType(FriendApply.AddTypeDesc.appAdd.getValue());
            friendAccept.setCode(String.valueOf(friendUserId));
            insertFriend(friendAccept);

            UserInfo user = userService.selectByPrimaryKey(userId);
            ChatList chatList = new ChatList();
            chatList.setFriendId(friendUserId);
            chatListService.createChat(user, chatList);

        }

    }


}
