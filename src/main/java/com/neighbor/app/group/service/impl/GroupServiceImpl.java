package com.neighbor.app.group.service.impl;

import com.neighbor.app.chatlist.entity.ChatList;
import com.neighbor.app.group.dao.GroupMapper;
import com.neighbor.app.group.dao.GroupMemberMapper;
import com.neighbor.app.group.entity.Group;
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
}
