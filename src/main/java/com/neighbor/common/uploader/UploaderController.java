package com.neighbor.common.uploader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;
import com.neighbor.common.constants.EnvConstants;
import com.neighbor.common.exception.UploaderException;
import com.neighbor.common.util.FileUploadUtil;
import com.neighbor.common.util.ResponseResult;

@Controller
@RequestMapping(value = "/uploader")
@SessionAttributes("user")
public class UploaderController {
    private static final Logger logger = LoggerFactory.getLogger(UploaderController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private Environment env;

    @RequestMapping(value = "/saveImg.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult saveImg(@ModelAttribute("user") UserInfo user, String fileType, Long friendId,
                                  String fileName, MultipartFile file) throws Exception {
        logger.info("saveImg file >>>> " + file);
        logger.info("friendId >> " + friendId);
        String url = saveImage(user, fileType, friendId, fileName, file);
        ResponseResult result = new ResponseResult();
        result.addBody("url", url);
        return result;
    }

    private String saveImage(UserInfo user, String fileType, Long friendId, String fileName, MultipartFile file)
            throws UploaderException {
        try {
            String retPath = null;
            String filepath = null;
            if (fileType != null) {
                retPath = FileUploadUtil.split + FileUploadUtil.IMAGE + FileUploadUtil.split
                        + UploaderImgType.getDesByValue(fileType) + FileUploadUtil.split + user.getId()
                        + FileUploadUtil.split;
            }
            if ((fileType == null && friendId != null && friendId != null)
                    || UploaderImgType.chat.toString().equals(fileType)) {
                retPath = FileUploadUtil.chatImagePath(user.getId(), friendId);
            }
            filepath = env.getProperty(EnvConstants.UPLOADER_FILEPATH) + retPath;
            FileUploadUtil.writeUploadFile(file, filepath, fileName);
            return retPath+fileName;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UploaderException();
        }
    }

    @RequestMapping(value = "/saveAvatarImg.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult saveAvatarImg(@ModelAttribute("user") UserInfo user, String fileType, Long friendId,
                                        String fileName, MultipartFile file) throws Exception {

        String url = saveImage(user, fileType, friendId, fileName, file);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserID(user.getId());
        userInfo.setUserPhoto(url);
        userService.updateByPrimaryKeySelective(userInfo);
        ResponseResult result = new ResponseResult();
        result.addBody("url", url);
        return result;
    }
}
