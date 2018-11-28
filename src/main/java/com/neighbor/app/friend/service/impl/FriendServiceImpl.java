package com.neighbor.app.friend.service.impl;

import com.neighbor.app.bankcard.entity.BankCard;
import com.neighbor.app.friend.dao.FriendApplyMapper;
import com.neighbor.app.friend.dao.FriendMapper;
import com.neighbor.app.friend.entity.Friend;
import com.neighbor.app.friend.entity.FriendApply;
import com.neighbor.app.friend.service.FriendService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {
    private static final Logger logger = LoggerFactory.getLogger(FriendServiceImpl.class);

    @Autowired
    private FriendMapper friendMapper;

    @Autowired
    private FriendApplyMapper friendApplyMapper;



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

    public void insertFriendApply(FriendApply friendApply) throws Exception {
        friendApplyMapper.insertFriendApply(friendApply);
    }


}
