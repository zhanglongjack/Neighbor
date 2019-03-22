package com.neighbor.common.uploader;

import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;
import com.neighbor.common.constants.EnvConstants;
import com.neighbor.common.exception.UploaderException;
import com.neighbor.common.util.FileUploadUtil;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Controller
@RequestMapping(value = "/uploader")
@SessionAttributes("user")
public class UploaderController {
    private static final Logger logger = LoggerFactory.getLogger(UploaderController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private Environment env;

    @RequestMapping(value = "/imgView.ser")
    public String imgView(String imgSrc, String modifyModel, Model model) throws Exception {
        logger.debug("primaryModalView request:" + imgSrc + ",model:" + model);
        model.addAttribute("modifyModel", modifyModel);
        model.addAttribute("imgSrc", imgSrc);
        logger.debug("primaryModalView model : " + model);

        return "page/common/ImageModal";
    }
     
    @RequestMapping(value = "/saveImg.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult saveImg(@ModelAttribute("user") UserInfo user, String fileType, Long friendId,
                                  String fileName, MultipartFile file) throws Exception {
        logger.info("saveImg file >>>> " + file);
        logger.info("friendId >> " + friendId);
        String url = saveImage(user.getId()+"", fileType, friendId, fileName, file);
        ResponseResult result = new ResponseResult();
        result.addBody("url", url);
        return result;
    }

    private String saveImage(String rootPath, String fileType, Long friendId, String fileName, MultipartFile file)
            throws UploaderException {
        try {
            String retPath = null;
            String filepath = null;
            if (fileType != null) {
                retPath = FileUploadUtil.split + FileUploadUtil.IMAGE + FileUploadUtil.split
                        + UploaderImgType.getDesByValue(fileType) + FileUploadUtil.split + rootPath
                        + FileUploadUtil.split;
            }
            if ((fileType == null && friendId != null && friendId != null)
                    || UploaderImgType.chat.toString().equals(fileType)) {
                retPath = FileUploadUtil.chatImagePath(rootPath, friendId);
            }
            filepath = env.getProperty(EnvConstants.UPLOADER_FILEPATH) + retPath;
            FileUploadUtil.writeUploadFile(file, filepath, fileName);
            return retPath + fileName;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UploaderException();
        }
    }

    @RequestMapping(value = "/saveAvatarImg.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult saveAvatarImg(@ModelAttribute("user") UserInfo user, String fileType, Long friendId,
                                        String fileName, MultipartFile file) throws Exception {

        String url = saveImage(user.getId()+"", fileType, friendId, fileName, file);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserID(user.getId());
        userInfo.setUserPhoto(url);
        userService.updateByPrimaryKeySelective(userInfo);
        ResponseResult result = new ResponseResult();
        result.addBody("url", url);
        return result;
    }
    
    @RequestMapping(value = "/saveRobotAvatarImg.ser", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult saveRobotAvatarImg(@RequestParam("file") MultipartFile file) throws Exception {
    	logger.info("saveRobotAvatarImg file >>>> " + file);
    	String filename = file.getOriginalFilename();
    	String suffix=filename.substring(filename.lastIndexOf("."));
    	String newFileName = "robot_"+System.currentTimeMillis()+""+UUID.randomUUID().toString()+""+suffix;
    	String url = saveImage("robot", newFileName, file); 
    	ResponseResult result = new ResponseResult();
    	result.addBody("url", url);
    	return result;
    }

    @RequestMapping(value = "/saveProductImg.ser", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult saveProductImg(String suffix, String imgType, MultipartFile file) throws Exception {
        logger.info("saveProductImg file >>>> " + file);
        String fileName = "t" + imgType + "--" + UUID.randomUUID().toString() + suffix;
        String url = saveImage("product", fileName, file);
        ResponseResult result = new ResponseResult();
        result.addBody("url", url);
        return result;
    }

    @RequestMapping(value = "/saveCmsImg.ser", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult saveCmsImg(String suffix, String position, MultipartFile file) throws Exception {
        logger.info("saveCmsImg file >>>> " + file);
        String fileName = "t" + position + "--" + UUID.randomUUID().toString() + suffix;
        String url = saveImage("cms", fileName, file);
        ResponseResult result = new ResponseResult();
        result.addBody("url", url);
        return result;
    }

    private String saveImage(String fileType, String fileName, MultipartFile file)
            throws UploaderException {
        try {
            String retPath = FileUploadUtil.split + FileUploadUtil.IMAGE + FileUploadUtil.split + fileType + FileUploadUtil.split;

            String filepath = env.getProperty(EnvConstants.UPLOADER_FILEPATH) + retPath;
            FileUploadUtil.writeUploadFile(file, filepath, fileName);
            return retPath + fileName;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UploaderException();
        }
    }
}
