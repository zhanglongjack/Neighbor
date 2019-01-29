package com.neighbor.app.friend.controller;

import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.friend.constants.FriendStatesDesc;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(value = "/friend")
@SessionAttributes("user")
public class FriendController {
    private static final Logger logger = LoggerFactory.getLogger(FriendController.class);
    //这样设计，是为了以后可以定时清除过期的二维码信息
    static Map<Long, Map<String, String>> userDateQrCodeMap = new HashMap<>();
    static Map<String, Long> qrCodeUserMap = new HashMap<>();

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserService userService;

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

    @RequestMapping(value = "/getQrCode.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult getQrCode(@ModelAttribute("user") UserInfo user) {
        logger.info("getQrCode begin ......");

        ResponseResult result = null;
        try {
            Long userId = user.getId();

            String qrCode = UUID.randomUUID().toString();
            HashMap<String, String> dateQrcodeMap = new HashMap<>();
            String currendDateStr = DateUtils.formatDateStr(new Date(), DateFormateType.TIGHT_SHORT_FORMAT);
            dateQrcodeMap.put(currendDateStr, qrCode);
            userDateQrCodeMap.put(userId, dateQrcodeMap);
            qrCodeUserMap.put(qrCode, userId);

            result = new ResponseResult();
            result.addBody("qrCode", qrCode);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

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
            if (userFriend == null) {
                userFriend = userService.selectByUserPhone(String.valueOf(friend.getFriendUserId()));
            }

            if (userFriend != null) {
                Friend f = new Friend();
                f.setUserId(user.getId());
                f.setFriendUserId(userFriend.getId());
                f.setStates(FriendStatesDesc.normal.getDes());
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

    @RequestMapping(value = "/checkQrCode.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult checkQrCode(@ModelAttribute("user") UserInfo user, String qrCode) {
        logger.info("checkQrCode begin ......");

        ResponseResult result = new ResponseResult();

        try {
            Long friendUserId = qrCodeUserMap.get(qrCode);

            if (friendUserId != null) {
                UserInfo userFriend = userService.selectByPrimaryKey(friendUserId);
                Friend friendReturn = null;
                if (userFriend == null) {
                    userFriend = userService.selectByUserPhone(String.valueOf(friendUserId));
                }

                if (userFriend != null) {
                    Friend f = new Friend();
                    f.setUserId(user.getId());
                    f.setFriendUserId(userFriend.getId());
                    f.setStates(FriendStatesDesc.normal.getDes());
                    friendReturn = friendService.viewFriendByUserIdAndFriendId(f);
                }

                result.addBody("user", userFriend);
                result.addBody("friend", friendReturn);
            } else {
                result.setErrorCode(2);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(ErrorCodeDesc.failed.getValue());
        }

        return result;
    }

    @RequestMapping(value = "/addFriend.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult addFriend(@ModelAttribute("user") UserInfo user, Friend friend) {
        logger.info("addFriend begin ......");

        ResponseResult result = new ResponseResult();
        try {

            Long userId = user.getId();
            Long friendUserId = friend.getFriendUserId();

            FriendApply friendApplyCheck = new FriendApply();
            friendApplyCheck.setUserId(userId);
            friendApplyCheck.setFriendUserId(friendUserId);
            FriendApply friendApplyOld = friendService.viewFriendApplyByUserIdAndFriendId(friendApplyCheck);

            if (friendApplyOld != null && friendApplyOld.getStates().equals("2")) {
                Friend friendOld = new Friend();
                friendOld.setUserId(friendApplyOld.getUserId());
                friendOld.setFriendUserId(friendApplyOld.getFriendUserId());
                friendOld.setStates(FriendStatesDesc.normal.getDes());
                friendOld = friendService.viewFriendByUserIdAndFriendId(friendOld);
                if (friendOld == null) {
                    friendService.deleteFriendApplyByPrimaryKey(friendApplyOld.getId());
                    friendApplyOld = null;
                }
            }

            if (friendApplyOld != null) {
                result.setErrorCode(2);
            } else {
                Date currentTime = new Date();
                FriendApply friendApplyNew = new FriendApply();
                friendApplyNew.setCreateTime(currentTime);
                friendApplyNew.setUpdateTime(currentTime);
                friendApplyNew.setContactDate(DateUtils.formatDateStr(currentTime, DateFormateType.SHORT_FORMAT));
                friendApplyNew.setContactTime(DateUtils.formatDateStr(currentTime, DateFormateType.TIME_FORMAT));
                friendApplyNew.setUserId(userId);
                friendApplyNew.setFriendUserId(friendUserId);
                friendApplyNew.setStates(FriendApply.StatesDesc.applyIng.getValue());
                friendApplyNew.setAddDirection(FriendApply.AddDirectionDesc.activeAdd.getValue());
                friendApplyNew.setAddType(FriendApply.AddTypeDesc.appAdd.getValue());

                friendService.insertFriendApply(friendApplyNew);

            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(ErrorCodeDesc.failed.getValue());
        }

        return result;
    }

    @RequestMapping(value = "/deleteFriend.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult deleteFriend(@ModelAttribute("user") UserInfo user, Friend friend) {
        logger.info("deleteFriend begin ......");

        ResponseResult result = new ResponseResult();
        try {

            friend.setUserId(user.getId());

            friendService.deleteFriend(friend);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(ErrorCodeDesc.failed.getValue());
        }

        return result;
    }

    @RequestMapping(value = "/acceptFriend.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult acceptFriend(@ModelAttribute("user") UserInfo user, Friend friend) {
        logger.info("acceptFriend begin ......");

        ResponseResult result = new ResponseResult();
        try {

            Long friendUserId = user.getId();
            Long userId = friend.getUserId();

            FriendApply friendApplyCheck = new FriendApply();
            friendApplyCheck.setUserId(userId);
            friendApplyCheck.setFriendUserId(friendUserId);
            FriendApply friendApplyOld = friendService.viewFriendApplyByUserIdAndFriendId(friendApplyCheck);

            if (friendApplyOld != null) {

                friendService.acceptFriend(friendApplyOld);

            } else {
                result.setErrorCode(ErrorCodeDesc.failed.getValue());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(ErrorCodeDesc.failed.getValue());
        }

        return result;
    }

}
