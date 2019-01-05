package com.neighbor.common.websoket.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);
	
	@Autowired
	private WebSocketInterceptor webSocketInterceptor;
	@Autowired
	private WebSocketPushHandler webSocketPushHandler;
	@Autowired
	private GroupMsgPushHandler groupMsgPushHandler;
//	@Autowired
//	private NoticeMsgHandler noticeMsgHandler;
	
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    	logger.info("初始化websoket");
    	registry.addHandler(webSocketPushHandler, "/chatServer")
//    			.addHandler(noticeMsgHandler, "/noticeServer")
    			.addHandler(groupMsgPushHandler, "/groupChatServer")
        		.addInterceptors(webSocketInterceptor).setAllowedOrigins("*");
        
        registry.addHandler(webSocketPushHandler, "/sockjs/chatServer")
        		.addHandler(groupMsgPushHandler, "/sockjs/groupChatServer")
//        		.addHandler(noticeMsgHandler, "/noticeServer")
                .addInterceptors(webSocketInterceptor).withSockJS();
        
//    	registry.addHandler(webSocketPushHandler, "/noticeServer")
//				.addInterceptors(webSocketInterceptor).setAllowedOrigins("*");
//
//    	registry.addHandler(webSocketPushHandler, "/sockjs/noticeServer")
//		        .addInterceptors(webSocketInterceptor).withSockJS();
        
    }
//
//    @Bean
//    public WebSocketHandler webSocketPushHandler() {
//        return new WebSocketPushHandler();
//    }

}
