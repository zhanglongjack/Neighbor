package com.neighbor.app.cms.controller;

import com.neighbor.app.cms.entity.Cms;
import com.neighbor.app.cms.service.CmsService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/cms")
@SessionAttributes("user")
public class CmsController {
    private static final Logger logger = LoggerFactory.getLogger(CmsController.class);

    @Autowired
    private CmsService cmsService;

    @RequestMapping(value = "/list.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult list(@ModelAttribute("user") UserInfo user, PageTools pageTools,Cms cms) throws Exception {
        logger.info("list request user >>>> " + user);
        cms.setPageTools(pageTools);
        ResponseResult result = cmsService.list(user, cms);
        return result;
    }

}
