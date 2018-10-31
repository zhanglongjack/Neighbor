package com.neighbor.app.api.control;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.neighbor.app.users.controller.IndexController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@Controller
//@RequestMapping("/")
public class ApiController {
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    @RequestMapping(value = "/api")
    public void service(HttpServletRequest request, HttpServletResponse response) {

        try {
            InputStream inputStream = request.getInputStream();
            String reqPostBody = IOUtils.toString(inputStream, "UTF-8");
            logger.info("reqPostBody===================>>" + reqPostBody + "--------");

            String requestString = request.getQueryString();
            logger.info("request query string===================>>" + requestString + "--------");

 

          /*  Object obj = SpringUtil.getBeanFactory().getBean(reqH);
            if (obj != null && obj instanceof CommonHandle) {
                CommonHandle c = (CommonHandle) obj;
                c.execute(request, response);
            } else {
                System.out.println("---no such service_name!-------");
            }
*/

            String returnContent = "{测试json返回数据。。。。。。。。。haha}";

            System.out.println("returnContent===================>>" + returnContent + "--------");

            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/json; charset=utf-8");
            response.getWriter().println(returnContent);


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("end------------------");
    }


}