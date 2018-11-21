package com.neighbor.common.websoket;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * 此类用来获取登录用户信息并交由websocket管理
 */
public class WebSocketInterceptor implements HandshakeInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketInterceptor.class);

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse arg1, WebSocketHandler arg2,
			Map<String, Object> arg3) throws Exception {
		logger.info("第一次握手");
		// 将ServerHttpRequest转换成request请求相关的类，用来获取request域中的用户信息
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			HttpServletRequest httpRequest = servletRequest.getServletRequest();
			logger.debug("有发送的用信息么?" + httpRequest.getParameter("name"));
		}

		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2, Exception arg3) {
		logger.info("握手结束");

	}

}
