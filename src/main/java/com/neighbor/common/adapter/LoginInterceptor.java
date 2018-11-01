package com.neighbor.common.adapter;
 

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.neighbor.app.users.entity.UserInfo;

/**
 * 登录验证拦截
 * 
 */
@Controller
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	/*
	 * @Autowired UserService userService;
	 */

	/*
	 * @Value("${IGNORE_LOGIN}") Boolean IGNORE_LOGIN;
	 */

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String basePath = request.getContextPath();
		String path = request.getRequestURI();
		logger.info("basePath:"+basePath+" -------  path:"+path);

		if (!doLoginInterceptor(path, basePath)) {// 是否进行登陆拦截
			return true;
		}

//		 HttpSession session = request.getSession();
//		 int userID = 2;
//		 UserInfo userInfo = sysUserService.getUserInfoByUserID(userID);
//		 System.out.println(JsonUtil.toJson(userInfo));
//		 session.setAttribute(Constants.SessionKey.USER, userInfo);
//
//		 Map<Object, Object> result = FileReaderUtil.readProperties("");
//			
//			for(Object key: result.keySet()){
//				if(key.equals(userId) && result.get(key).equals(password)){
//					return mv;
//				}
//			}
			
		// 如果登录了，会把用户信息存进session
		HttpSession session = request.getSession();
		UserInfo user = (UserInfo) session.getAttribute("user");
		/*
		 * User userInfo = new User(); userInfo.setId(users.get(0).getId());
		 * userInfo.setName(users.get(0).getName());
		 * userInfo.setPassword(users.get(0).getPassword());
		 */
		// 开发环节的设置，不登录的情况下自动登录
		/*
		 * if(userInfo==null && IGNORE_LOGIN){ userInfo =
		 * sysUserService.getUserInfoByUserID(2);
		 * session.setAttribute(Constants.SessionKey.USER, userInfo); }
		 */
		if (user == null) {
			/*
			 * log.info("尚未登录，跳转到登录界面");
			 * response.sendRedirect(request.getContextPath()+"signin");
			 */

			String requestType = request.getHeader("X-Requested-With");
			// System.out.println(requestType);
			if (requestType != null && requestType.equals("XMLHttpRequest")) {
				response.setHeader("sessionstatus", "timeout");
				// response.setHeader("basePath",request.getContextPath());
				response.getWriter().print("LoginTimeout");
				return false;
			} else {
				logger.info("尚未登录，跳转到登录界面");
				response.sendRedirect("/login.html");
			}
			return false;
		}
		logger.info("用户已登录,userName:"+user.getName());
		return true;
	}

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
//		 notLoginPaths.add("/");
//		notLoginPaths.add("/index");
		notLoginPaths.add("/test.html");
		notLoginPaths.add("/login.html");
		notLoginPaths.add("/shutdown");
		notLoginPaths.add("/login");
		notLoginPaths.add("/register");
		notLoginPaths.add("/kaptcha.jpg");
		notLoginPaths.add("/kaptcha");
		notLoginPaths.add("/api");
		// notLoginPaths.add("/sys/logout");
		// notLoginPaths.add("/loginTimeout");

		if (notLoginPaths.contains(path))
			return false;
		return true;
	}
}