package com.neighbor.app.group.controller;

import com.neighbor.app.chatlist.entity.ChatList;
import com.neighbor.app.group.entity.Group;
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
    public ResponseResult createGroup(@ModelAttribute("user") UserInfo user, Group group, PageTools pageTools) throws Exception{
        logger.info("chatlist request user >>>> " + user);
        logger.info("group >>>> " + group);
        logger.info("pageTools >>>> " + pageTools);
        group.setPageTools(pageTools);
        ResponseResult result  = groupService.chatlist(user,group);
        return result;
    }

}
