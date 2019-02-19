package com.neighbor.app.cms.controller;

import com.neighbor.app.cms.entity.Cms;
import com.neighbor.app.cms.service.CmsService;
import com.neighbor.app.product.entity.Product;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

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

    @RequestMapping(value = "/pageView.ser")
    @ResponseBody
    public Map<String, Object> pageView(Cms cms, PageTools pageTools, @ModelAttribute("user") UserInfo user) throws Exception {
        logger.debug("ProductController pageView : " + user);

//        if (!user.isAdmin()) {
//            userInfo.setUserID(user.getId());
//        }
        Long size = ((PageTools) (cmsService.list(null, cms).getBody().get("pageTools"))).getTotal();
        pageTools.setTotal(size);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("pageTools", pageTools);
        return result;
    }

    @RequestMapping(value = "/loadPage.ser")
    public ModelAndView loadPage(Cms cms, PageTools pageTools,
                                 @ModelAttribute("user") UserInfo user) throws Exception {
        logger.debug("ProductController loadPage :" + cms + " page info ===" + pageTools);

//        if (!user.isAdmin()) {
//            userInfo.setUserID(user.getId());
//        }

        Map<String, Object> body = cmsService.list(null, cms).getBody();

        cms.setPageTools(pageTools);
        ModelAndView mv = new ModelAndView("page/cms/Content :: container-fluid");
        Long size = ((PageTools) (body.get("pageTools"))).getTotal();
        pageTools.setTotal(size);

        mv.addObject("resultList", body.get("resultList"));
        mv.addObject("pageTools", pageTools);
        mv.addObject("queryObject", cms);

        return mv;
    }

}
