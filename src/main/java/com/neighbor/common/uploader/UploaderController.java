package com.neighbor.common.uploader;

import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.constants.EnvConstants;
import com.neighbor.common.exception.UploaderException;
import com.neighbor.common.util.FileUploadUtil;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
@RequestMapping(value = "/uploader")
@SessionAttributes("user")
public class UploaderController {
    private static final Logger logger = LoggerFactory.getLogger(UploaderController.class);

    @Autowired
    private Environment env;

    @RequestMapping(value = "/saveImg.req",method= RequestMethod.POST)
    @ResponseBody
    public ResponseResult saveImg(@ModelAttribute("user") UserInfo user,String fileType,Long friendId,String fileName, MultipartFile file) throws Exception{
        logger.info("saveImg file >>>> " + file);
        logger.info("friendId >> "+friendId);
       /* logger.info("user info >>>> " + user);*/
        ResponseResult result = null;
        try {
            result = new ResponseResult();
            String retPath = null;
            String filepath = null;
            if(fileType!=null){
                retPath = FileUploadUtil.split+FileUploadUtil.IMAGE+FileUploadUtil.split+UploaderImgType.getDesByValue(fileType)+FileUploadUtil.split+user.getId()+FileUploadUtil.split;
            }
            if((fileType==null&&friendId!=null&&friendId!=null)||UploaderImgType.chat.toString().equals(fileType)){
                retPath = FileUploadUtil.chatImagePath(user.getId(),friendId);
            }
            filepath = env.getProperty(EnvConstants.UPLOADER_FILEPATH)+retPath;
            FileUploadUtil.writeUploadFile(file,filepath,fileName);
            result.addBody("url",retPath);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new UploaderException();
        }
        return result;
    }
}
