package com.neighbor.common.security;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.neighbor.app.users.constants.UserContainer;
import com.neighbor.app.users.entity.UserInfo;

@Component
public class TokenLoginInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(TokenLoginInterceptor.class);
	@Autowired
	private UserContainer userContainer;
	
//	@Autowired
//	private UserService userService;
	
	/**
	 * 是否进行登陆过滤
	 * 
	 * @param path
	 * @param basePath
	 * @return
	 */
	private boolean doLoginInterceptor(String path, String basePath) {
		path = path.substring(basePath.length());
		Set<String> notLoginPaths = new HashSet<>();
		// 设置不进行登录拦截的路径：登录注册和验证码
		notLoginPaths.add("/accountLogin.req");
		notLoginPaths.add("/registerLogin.req");  
		notLoginPaths.add("/sendSMS.req");  

		if (notLoginPaths.contains(path))
			return false;
		return true;
	}
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String basePath = request.getContextPath();
		String path = request.getRequestURI();
		logger.info("basePath:"+basePath+" -------  path:"+path);

		if (!doLoginInterceptor(path, basePath)) {// 是否进行登陆拦截
			logger.info("不需要拦截的路径:{}",path);
			return true;
		}
		String token = request.getParameter("token");
		// 检查token是否为空
		if (!StringUtils.hasLength(token)) {
			logger.info("token 不存在");
			printJson(response, "");
			return false;
		}
		
		UserInfo user = (UserInfo) userContainer.get(token);
		
		if (user == null) {
			logger.info("token:{},用户 不存在:{}",token,userContainer.userMap);
			printJson(response, "");
			return false;
		}
		logger.info("用户已经登录:"+user);
		HttpSession session = request.getSession();
		session.setAttribute("user", user);

		
//		Long userId = user.getId();
		
//		UserInfo resultUser = userService.selectByPrimaryKey(userId);
		
		
		
//		String enterpriseId = getUserEnterpriseService().selectEnterpriseByUser(userId);
//		if (enterpriseId == null) {
////			getRedisService().set(REDIS_USER_SESSION_KEY + ":" + token, resultUser, SSO_SESSION_EXPIRE);
//			// 这里是为了更新  过期时间
//			return true;
//		}
//		Enterprise sqlEnterprise = getEnterpriseService().selectEnterpriseById(enterpriseId);
//		getRedisService().set(REDIS_USER_SESSION_KEY + ":" + token, resultUser, SSO_SESSION_EXPIRE);
//		getRedisService().set(REDIS_ENTERPRISE_SESSION_KEY + ":" + token, sqlEnterprise, SSO_SESSION_EXPIRE);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
 

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Requested-With, token");
		response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, OPTIONS, POST, PUT, DELETE");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Max-Age", "3600");
		// vary: Origin
		// Vary: Access-Control-Request-Method
		// Vary: Access-Control-Request-Headers
		// Access-Control-Allow-Origin: * 
	}

	private static void printJson(HttpServletResponse response, String code) {
//		ResponseResult responseResult = new ResponseResult(code, false, "token过期,请重新登陆", null);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tokenCheck", false);
		map.put("tokenMsg", "token过期,请重新登陆");
		String content = JSON.toJSONString(map);
		printContent(response, content);
	}
//
	private static void printContent(HttpServletResponse response, String content) {
		logger.debug(content);
		try {
			response.reset();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter();
			pw.write(content);
			pw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}