package com.neighbor.common.util;

import com.neighbor.common.exception.UploaderException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUploadUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);
    public static final String IMAGE = "image";
    public static final String CHAT = "chat";
    public static final String split="/";
    /**
     * 文件上传处理
     *
     * @param file
     * @return
     */
    public static String writeUploadFile(MultipartFile file, String filepath,String fileName) throws UploaderException {
        String filename = null;
        try {
            filename = file.getOriginalFilename();
            String realpath = filepath;
            logger.info("realpath >> "+realpath);
            File fileDir = new File(realpath);
            if (!fileDir.exists())
                fileDir.mkdirs();

            String extname = FilenameUtils.getExtension(filename);
            String allowImgFormat = "gif,jpg,jpeg,png";
            if (!allowImgFormat.contains(extname.toLowerCase())) {
                return "NOT_IMAGE";
            }

            filename = fileName;
            InputStream input = null;
            FileOutputStream fos = null;
            try {
                input = file.getInputStream();
                fos = new FileOutputStream(realpath + "/" + filename);
                IOUtils.copy(input, fos);
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
                throw new UploaderException();
            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(fos);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new UploaderException();
        }
        return filename;
    }

    public static String chatImagePath(Long godId,Long friendId){
        return split+FileUploadUtil.IMAGE+split+FileUploadUtil.CHAT+split+godId+split+friendId+split;
    }

}
