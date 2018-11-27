package com.neighbor.app.friend.controller;

import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.chatlist.entity.ChatList;
import com.neighbor.app.chatlist.service.ChatListService;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping(value = "/friend")
@SessionAttributes("user")
public class FriendController {
    private static final Logger logger = LoggerFactory.getLogger(FriendController.class);

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatListService chatListService;

    @RequestMapping(value = "/listRecord.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult listRecord(@ModelAttribute("user") UserInfo user, PageTools pageTools) throws Exception {
        logger.info("listRecord request user >>>> " + user);
        Friend friend = new Friend();
        friend.setPageTools(pageTools);
        ResponseResult result = friendService.listRecord(user, friend);
        return result;
    }

    @RequestMapping(value = "/listFriendApplyRecord.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult listFriendApplyRecord(@ModelAttribute("user") UserInfo user, PageTools pageTools) throws Exception {
        logger.info("listFriendApplyRecord request user >>>> " + user);
        FriendApply friendApply = new FriendApply();
        friendApply.setPageTools(pageTools);
        ResponseResult result = friendService.listFriendApplyRecord(user, friendApply);
        return result;
    }

    @RequestMapping(value = "/getFriend.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult getFriend(@ModelAttribute("user") UserInfo user, Friend friend) {
        logger.info("getUserInfoById begin ......");

        ResponseResult result = null;
        try {
            UserInfo userFriend = userService.selectByPrimaryKey(friend.getFriendUserId());
            Friend friendReturn = null;
            if (userFriend != null) {
                Friend f = new Friend();
                f.setUserId(user.getId());
                f.setFriendUserId(userFriend.getId());
                friendReturn = friendService.viewFriendByUserIdAndFriendId(f);
            }
            result = new ResponseResult();
            result.addBody("user", userFriend);
            result.addBody("friend", friendReturn);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return result;
    }

    @RequestMapping(value = "/addFriend.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult addFriend(@ModelAttribute("user") UserInfo user,Friend friend) {
        logger.info("addFriend begin ......");

        ResponseResult result = new ResponseResult();
        try {

            Long userId = user.getId();
            Long friendUserId = friend.getFriendUserId();

            FriendApply friendApplyCheck = new FriendApply();
            friendApplyCheck.setUserId(userId);
            friendApplyCheck.setFriendUserId(friendUserId);
            FriendApply friendApplyOld = friendService.viewFriendApplyByUserIdAndFriendId(friendApplyCheck);

            if(friendApplyOld!=null){
                result.setErrorCode(2);
            }else{
                Date currentTime = new Date();
                FriendApply friendApplyNew = new FriendApply();
                friendApplyNew.setCreateTime(currentTime);
                friendApplyNew.setUpdateTime(currentTime);
                friendApplyNew.setContactDate(DateUtils.formatDateStr(currentTime, DateFormateType.SHORT_FORMAT));
                friendApplyNew.setContactTime(DateUtils.formatDateStr(currentTime, DateFormateType.TIME_FORMAT));
                friendApplyNew.setUserId(userId);
                friendApplyNew.setFriendUserId(friendUserId);
                friendApplyNew.setStates(FriendApply.StatesDesc.申请中.getValue());
                friendApplyNew.setAddDirection(FriendApply.AddDirectionDesc.主动添加.getValue());
                friendApplyNew.setAddType(FriendApply.AddTypeDesc.APP添加.getValue());

                friendService.insertFriendApply(friendApplyNew);
                ChatList chatList = new ChatList();
                chatList.setFriendId(friendUserId);
                chatListService.createChat(user,chatList);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(ErrorCodeDesc.failed.getValue());
        }

        return result;
    }

}
