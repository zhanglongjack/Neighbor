package com.neighbor.app.download;

import com.neighbor.app.sys.entity.SysVersion;
import com.neighbor.app.sys.service.SysService;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

@Controller
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private Environment env;

    @Autowired
    private SysService sysService;

    /**
     * 文件下载
     * <p>
     *
     * @param response
     * @return
     */
    @RequestMapping(value = "/fileDownload.req")
    public String download(HttpServletResponse response) {

        Thread thread = Thread.currentThread();
        logger.info("download----------id>>" + thread.getId() + "---name:" + thread.getName() + "===");

        String fileName = env.getProperty("download.file.name");
        String filepath = env.getProperty("download.file.path") + "/" + fileName;

        try {
            response.setContentType("application/octet-stream"); //设置内容类型为下载类型
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);//设置下载的文件名称
            OutputStream out = response.getOutputStream();   //页面返回方式为输出流

            File file = new File(filepath);  //创建文件
            FileInputStream fis = new FileInputStream(file);  //创建文件字节输入流
            BufferedInputStream bis = new BufferedInputStream(fis); //创建文件缓冲输入流
            byte[] buffer = new byte[bis.available()];//从输入流中读取不受阻塞
            bis.read(buffer);//读取数据文件
            bis.close();

            try {
                out.write(buffer);//输出数据文件
                out.flush();//释放缓存
                out.close();//关闭输出流
            } catch (Exception e) {
                //因为有些手机浏览器下载的时候会两次访问，造成日志错误信息，其实结果是可以正常下载
                // 这样就可以去除 多余的下载日志错误信息
                //
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @RequestMapping(value = "/checkVersion.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult checkVersion() {
        logger.info("checkVersion begin ......");

        ResponseResult result = null;
        try {
            SysVersion sysVersion = sysService.viewLastVersion();

            result = new ResponseResult();
            result.addBody("lastVersion", sysVersion.getVersion());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return result;
    }

}
