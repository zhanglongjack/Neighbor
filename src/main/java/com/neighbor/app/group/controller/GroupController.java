package com.neighbor.app.group.controller;

import com.neighbor.app.group.entity.Group;
import com.neighbor.app.group.entity.GroupApply;
import com.neighbor.app.group.entity.GroupMember;
import com.neighbor.app.group.service.GroupService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/group")
@SessionAttributes("user")
public class GroupController {
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/chatlist.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult chatlist(@ModelAttribute("user") UserInfo user, Group group, PageTools pageTools) throws Exception{
        logger.info("chatlist request user >>>> " + user);
        logger.info("group >>>> " + group);
        logger.info("pageTools >>>> " + pageTools);
        group.setPageTools(pageTools);
        ResponseResult result  = groupService.chatlist(user,group);
        return result;
    }

    @RequestMapping(value = "/viewinfo.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult viewinfo(Group group) throws Exception{
        logger.info("group >>>> " + group);
        ResponseResult result  = groupService.viewinfo(group);
        return result;
    }

    @RequestMapping(value = "/chatInfo.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult chatInfo(@ModelAttribute("user") UserInfo user, GroupMember groupMember, PageTools pageTools) throws Exception{
        logger.info("chatlist request user >>>> " + user);
        logger.info("groupMember >>>> " + groupMember);
        logger.info("pageTools >>>> " + pageTools);
        groupMember.setPageTools(pageTools);
        ResponseResult result  = groupService.chatInfo(user,groupMember);
        return result;
    }

    @RequestMapping(value = "/memberList.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult memberList(@ModelAttribute("user") UserInfo user, GroupMember groupMember, PageTools pageTools) throws Exception{
        logger.info("chatlist request user >>>> " + user);
        logger.info("groupMember >>>> " + groupMember);
        logger.info("pageTools >>>> " + pageTools);
        groupMember.setPageTools(pageTools);
        ResponseResult result  = groupService.memberList(user,groupMember);
        return result;
    }

    @RequestMapping(value = "/setting.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult setting(Group group) throws Exception{
        logger.info("group >>>> " + group);
        ResponseResult result  = groupService.setting(group);
        return result;
    }

    @RequestMapping(value = "/chatSetting.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult chatSetting(GroupMember groupMember) throws Exception{
        logger.info("groupMember >>>> " + groupMember);
        ResponseResult result  = groupService.chatSetting(groupMember);
        return result;
    }

    @RequestMapping(value = "/clearChatHistory.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult clearChatHistory(@ModelAttribute("user") UserInfo user, GroupMember groupMember) throws Exception{
        logger.info("chatlist request user >>>> " + user);
        logger.info("groupMember >>>> " + groupMember);
        ResponseResult result  = groupService.clearChatHistory(user,groupMember);
        return result;
    }

    @RequestMapping(value = "/exitGroup.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult exitGroup(@ModelAttribute("user") UserInfo user, GroupMember groupMember) throws Exception{
        logger.info("chatlist request user >>>> " + user);
        logger.info("groupMember >>>> " + groupMember);
        ResponseResult result  = groupService.exitGroup(user,groupMember);
        return result;
    }

    @RequestMapping(value = "/enterGroup.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult enterGroup(@ModelAttribute("user") UserInfo user, GroupMember groupMember) throws Exception{
        logger.info("chatlist request user >>>> " + user);
        logger.info("groupMember >>>> " + groupMember);
        ResponseResult result  = groupService.enterGroup(user,groupMember);
        return result;
    }


    //
    @RequestMapping(value = "/enterGroupApplyNum.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult enterGroupApplyNum(@ModelAttribute("user") UserInfo user, GroupApply groupApply) throws Exception{
        logger.info("chatlist request user >>>> " + user);
        logger.info("groupApply >>>> " + groupApply);
        ResponseResult result  = groupService.enterGroupApplyNum(user,groupApply);
        return result;
    }

    @RequestMapping(value = "/clearGroupApplyNum.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult clearGroupApplyNum(@ModelAttribute("user") UserInfo user, GroupApply groupApply) throws Exception{
        logger.info("chatlist request user >>>> " + user);
        logger.info("groupApply >>>> " + groupApply);
        ResponseResult result  = groupService.clearGroupApplyNum(user,groupApply);
        return result;
    }

    @RequestMapping(value = "/groupApplyRecord.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult groupApplyRecord(@ModelAttribute("user") UserInfo user, GroupApply groupApply) throws Exception{
        logger.info("chatlist request user >>>> " + user);
        logger.info("groupApply >>>> " + groupApply);
        ResponseResult result  = groupService.groupApplyRecord(user,groupApply);
        return result;
    }

    @RequestMapping(value = "/groupApply.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult groupApply(@ModelAttribute("user") UserInfo user, GroupApply groupApply) throws Exception{
        logger.info("chatlist request user >>>> " + user);
        logger.info("groupApply >>>> " + groupApply);
        ResponseResult result  = groupService.groupApply(user,groupApply);
        return result;
    }

}
