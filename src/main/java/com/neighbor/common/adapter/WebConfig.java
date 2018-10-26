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
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;  
  
@Configuration  
public class WebConfig extends WebMvcConfigurerAdapter {  
	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
    //@Autowired  
    //LogInterceptor logInterceptor;  
  
    @Autowired  
    LoginInterceptor loginInterceptor;  
  
    /** 
     * 不需要登录拦截的url:登录注册和验证码 
     */  
    final String[] notLoginInterceptPaths = {"/shutdown","/signin","/login/**","/register/**","/framework/**","/css/**","/js/**","/kaptcha.jpg/**","/kaptcha/**"};//"/", "/login/**", "/person/**", "/register/**", "/validcode", "/captchaCheck", "/file/**", "/contract/htmltopdf", "/questions/**", "/payLog/**", "/error/**" };  
  
    @Override  
    public void addInterceptors(InterceptorRegistry registry) {  
        // 日志拦截器  
        //registry.addInterceptor(logInterceptor).addPathPatterns("/**");  
        // 登录拦截器  
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**","","/").excludePathPatterns(notLoginInterceptPaths);  
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
//        registry.addViewController("/").setViewName("/page/index");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }
    

	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
	}

}  