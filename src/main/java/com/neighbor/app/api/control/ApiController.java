package com.neighbor.app.api.control;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.neighbor.app.api.common.BaseApiResp;
import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.api.common.SpringUtil;
import com.neighbor.app.api.handler.CommonHandler;

@Controller
//@RequestMapping("/")
public class ApiController {
	private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

	@RequestMapping(value = "/api",method=RequestMethod.POST)
	public void service(HttpServletRequest request, HttpServletResponse response) {
		BaseApiResp baseApiResp = new BaseApiResp();
		try {
			if(!"post".equals(request.getMethod().toLowerCase())) {
				baseApiResp.setErrorCode(ErrorCodeDesc.失败.getValue());
				baseApiResp.setErrorMessage("接口暂时只支持POST请求方式");
				sendMessage(response, baseApiResp);
				return;
			}
			InputStream inputStream = request.getInputStream();
			String reqPostBody = IOUtils.toString(inputStream, "UTF-8");
			if(StringUtils.isEmpty(reqPostBody)) {
				baseApiResp.setErrorCode(ErrorCodeDesc.失败.getValue());
				baseApiResp.setErrorMessage("消息内容(body)不可以为空!");
				sendMessage(response, baseApiResp);
				return;
			}
			logger.info("reqPostBody====>>" + reqPostBody + "--------");
		    //后续增加 各个接口参数校验
			String requestString = request.getQueryString();
			logger.info("request query string====>>" + requestString + "--------");

			String serviceName = JSON.parseObject(reqPostBody, BaseApiResp.class).getServiceName();
			if (StringUtils.isEmpty(serviceName)) {
				baseApiResp.setErrorCode(ErrorCodeDesc.失败.getValue());
				baseApiResp.setErrorMessage("serviceName 不能为空");
				sendMessage(response, baseApiResp);
				return;
			}
			logger.info("request serviceName ====>>" + serviceName + "--------");
			Object obj = SpringUtil.getBean(serviceName);
			if (obj != null && obj instanceof CommonHandler) {
				CommonHandler handler = (CommonHandler) obj;
				baseApiResp = handler.service(reqPostBody);
			} else {
				baseApiResp.setErrorCode(ErrorCodeDesc.失败.getValue());
				baseApiResp.setErrorMessage("serviceName 不能为空");
			}
			sendMessage(response, baseApiResp);

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			baseApiResp.setErrorCode(ErrorCodeDesc.失败.getValue());
			baseApiResp.setErrorMessage("系统开小差~请联系管理员！");
			try {
				sendMessage(response, baseApiResp);
			} catch (Exception e1) {
				logger.error(e1.getMessage(),e1);
			}

		}

	}

	public void sendMessage(HttpServletResponse response, BaseApiResp resp) throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json; charset=utf-8");
		String respStr = JSON.toJSONString(resp);
		logger.info("respStr <== "+respStr);
		response.getWriter().println(respStr);
	}

}