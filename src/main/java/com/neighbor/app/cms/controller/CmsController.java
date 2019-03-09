package com.neighbor.app.cms.controller;

import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.cms.entity.Cms;
import com.neighbor.app.cms.service.CmsService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.constants.EnvConstants;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/cms")
@SessionAttributes("user")
public class CmsController {
    private static final Logger logger = LoggerFactory.getLogger(CmsController.class);

    @Autowired
    private CmsService cmsService;

    @Autowired
    private Environment env;

    @RequestMapping(value = "/list.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult list(@ModelAttribute("user") UserInfo user, PageTools pageTools, Cms cms) throws Exception {
        logger.info("list request user >>>> " + user);
        cms.setPageTools(pageTools);
        ResponseResult result = cmsService.list(user, cms);
        return result;
    }

    @RequestMapping(value = "/pageView.ser")
    @ResponseBody
    public Map<String, Object> pageView(Cms cms, PageTools pageTools, @ModelAttribute("user") UserInfo user) throws Exception {
        logger.debug("CmsController pageView : " + user);

        Long size = ((PageTools) (cmsService.list(null, cms).getBody().get("pageTools"))).getTotal();
        pageTools.setTotal(size);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("pageTools", pageTools);
        return result;
    }

    @RequestMapping(value = "/loadPage.ser")
    public ModelAndView loadPage(Cms cms, PageTools pageTools,
                                 @ModelAttribute("user") UserInfo user) throws Exception {
        logger.debug("CmsController loadPage :" + cms + " page info ===" + pageTools);

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

    @RequestMapping(value = "/addModalView.ser")
    public String addModalView(Long id, Model model) throws Exception {
        logger.debug("addModalView request:" + id + ",model:" + model);

        return "page/cms/AddModal";
    }

    @RequestMapping(value = "/cmsAdd.ser")
    @ResponseBody
    public Map<String, Object> cmsAdd(UserInfo userInfo, Cms cms) throws Exception {
        logger.info("cmsAdd request:{}", cms);
        int num = cmsService.insertSelective(cms);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        map.put("addNumber", num);
        return map;
    }

    @RequestMapping(value = "/modifyModalView.ser")
    public String modifyModalView(Long id, Model model) throws Exception {
        logger.debug("modifyModalView request:" + id + ",model:" + model);
        if (id != null) {
            Cms cms = new Cms();
            cms.setId(id);
            cms = cmsService.viewCms(cms);
            model.addAttribute("modifyInfo", cms);
        }

        logger.debug("primaryModalView model : " + model);

        return "page/cms/ModifyModal";
    }

    @RequestMapping(value = "/cmsModify.ser")
    @ResponseBody
    public Map<String, Object> cmsModify(UserInfo userInfo, Cms cms) throws Exception {
        logger.info("cmsModify request:{}", cms);
        int num = cmsService.updateCms(cms);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        map.put("addNumber", num);
        return map;
    }

    @RequestMapping(value = "/deleteImg.ser", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult deleteImg(String imgUrl, Long id) {
        logger.info("deleteImg begin ......");

        ResponseResult result = new ResponseResult();
        try {

            String filepath = env.getProperty(EnvConstants.UPLOADER_FILEPATH) + imgUrl;
            File file = new File(filepath);
            file.delete();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(ErrorCodeDesc.failed.getValue());
        }

        return result;
    }

    @RequestMapping(value = "/deleteCms.ser", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult deleteCms(Long id) {
        logger.info("deleteCms begin ......");

        ResponseResult result = new ResponseResult();
        try {

            cmsService.deleteCms(id);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(ErrorCodeDesc.failed.getValue());
        }

        return result;
    }

}
