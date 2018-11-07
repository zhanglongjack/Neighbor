//package com.neighbor.common.security;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//import com.neighbor.security.permission.service.impl.CustomUserDetailsService;
//
////@Configuration
////@EnableWebSecurity
////@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//	private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
//	
//	@Autowired
//	private CustomUserDetailsService userDetailsService;
//
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		logger.debug("configure auth= "+ auth);
////		auth.userDetailsService(userDetailsService).passwordEncoder(new PasswordEncoder() {
////			 @Override
////             public String encode(CharSequence rawPassword) {
////                 return EncodeData.encode(rawPassword);
////             }
////
////             @Override
////             public boolean matches(CharSequence rawPassword, String encodedPassword) {
////                 return EncodeData.matches(rawPassword,encodedPassword);
////             }
////		});
//	}
//	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		logger.debug("configure http= "+ http);
//		http.authorizeRequests()
//				// 如果有允许匿名的url，填在下面
//				// .antMatchers().permitAll()
//				// 设置静态的资源允许所有访问
//		        .antMatchers("/static/neighbor/**").permitAll()
//				.anyRequest().authenticated().and()
//				// 设置登陆页
//				.formLogin().loginPage("/login")
//				// 设置登陆成功页
//				.successForwardUrl("/loginJsonResp").permitAll()
//				.failureUrl("/login/error").permitAll()
//				// 自定义登陆用户名和密码参数，默认为username和password
//				// .usernameParameter("username")
//				// .passwordParameter("password")
//             // 自动登录
//                .and().rememberMe()
////                .tokenRepository(persistentTokenRepository())
////                // 有效时间：单位s
////                .tokenValiditySeconds(60)
////                .userDetailsService(userDetailsService)
//				.and().logout().permitAll();
//
//		// 关闭CSRF跨域
//		http.csrf().disable();
//	}
//
//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		logger.debug("configure web= "+ web);
//		// 设置拦截忽略文件夹，可以对静态资源放行
//		web.ignoring().antMatchers("/neighbor/**","/framework/**","/css/**", "/js/**");
//	}
//}
