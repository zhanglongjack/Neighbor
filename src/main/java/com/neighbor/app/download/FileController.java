package com.neighbor.app.download;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
public class FileController {

    @Autowired
    private Environment env;

    /**
     * 文件下载
     *
     * @param response
     * @return
     */
    @RequestMapping(value = "/fileDownload.req")
    @ResponseBody
    public String download(HttpServletResponse response,String downloadTokenId) {

        String fileName = env.getProperty("download.file.name");
        String filepath = env.getProperty("download.file.path");
        File file = new File(filepath, fileName);
        if (file.exists()) {

            byte[] buff = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os = null;
            try {
                // 配置文件下载
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                // 下载文件能正常显示中文
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

                os = response.getOutputStream();
                bis = new BufferedInputStream(new FileInputStream(file));
                int i = bis.read(buff);
                while (i != -1) {
                    os.write(buff, 0, buff.length);
                    os.flush();
                    i = bis.read(buff);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "文件下载失败";
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return "文件下载成功";
        } else {
            return "文件不存在";
        }
    }


}
