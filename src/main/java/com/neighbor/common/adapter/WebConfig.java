package com.neighbor.common.adapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;  
  
@Configuration  
public class WebConfig implements WebMvcConfigurer {  
	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
  
    @Autowired  
    LoginInterceptor loginInterceptor;  
  
    /** 
     * 不需要登录拦截的url:登录注册和验证码 
     */  
	final String[] notLoginInterceptPaths = 
		{ 
			"/error/**",
			"/shutdown", 
			"/signin", 
			"/login/**", 
			"/register/**", 
			"/validcode",
			"/framework/**",
			"/neighbor/**",
//			"/css/**", 
//			"/js/**" 
		};
	
	
	// "/", "/login/**", "/person/**",
									// "/register/**", "/validcode",
									// "/captchaCheck", "/file/**",
									// "/contract/htmltopdf", "/questions/**",
									// "/payLog/**", "/error/**" };  
  
    @Override  
    public void addInterceptors(InterceptorRegistry registry) {  
        // 日志拦截器  
        //registry.addInterceptor(logInterceptor).addPathPatterns("/**");  
        // 登录拦截器  
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(notLoginInterceptPaths);  
    }  
  
    @Override  
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {  
        configurer.enable();  
    }  
  
//    @Bean  
//    public InternalResourceViewResolver viewResolver() {  
//        InternalResourceViewResolver resolver = new InternalResourceViewResolver();  
//        resolver.setPrefix("/templates/");  
//        resolver.setSuffix(".html");  
//        resolver.setViewClass(JstlView.class);  
//        return resolver;  
//    }  
  
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    	registry.addRedirectViewController("/", "/index");
        registry.addViewController("/index").setViewName("page/index");
        registry.addViewController("/user").setViewName("page/user/UserCenter");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
    

	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/**");
	}

}  