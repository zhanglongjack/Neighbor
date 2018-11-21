package com.neighbor.common.websoket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.neighbor.app.users.controller.LoginController;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    	logger.info("初始化websoket");
        registry.addHandler(WebSocketPushHandler(), "/chatServer")
        		.addInterceptors(new WebSocketInterceptor()).setAllowedOrigins("*");
        
        registry.addHandler(WebSocketPushHandler(), "/sockjs/chatServer")
                .addInterceptors(new WebSocketInterceptor()).withSockJS();
    }

    @Bean
    public WebSocketHandler WebSocketPushHandler() {
        return new WebSocketPushHandler();
    }

}