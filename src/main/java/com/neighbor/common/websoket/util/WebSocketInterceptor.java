package com.neighbor.common.websoket.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.neighbor.app.users.constants.UserContainer;
import com.neighbor.app.users.entity.UserInfo;

/**
 * 此类用来获取登录用户信息并交由websocket管理
 */
@Component
public class WebSocketInterceptor implements HandshakeInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketInterceptor.class);
	@Autowired
	private UserContainer userContainer;

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler arg2,
			Map<String, Object> attributes) throws Exception {
		logger.info("第一次握手");
		// 将ServerHttpRequest转换成request请求相关的类，用来获取request域中的用户信息
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			HttpServletRequest httpRequest = servletRequest.getServletRequest();
			
			logger.debug("有发送的用户信息么?" + httpRequest.getParameter("token"));
			String token = httpRequest.getParameter("token");
			// 检查token是否为空
			if (!StringUtils.hasLength(token)) {
				logger.info("token 不能为空");
				return false;
			}
			
			UserInfo user = (UserInfo) userContainer.get(token);
			if(user!=null){
				attributes.put("user", user);
			}
		}

		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2, Exception arg3) {
		logger.info("握手结束"+arg3.getMessage());

	}

}
