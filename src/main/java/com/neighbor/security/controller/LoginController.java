package com.neighbor.security.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.neighbor.app.users.service.UserService;

//@Controller
public class LoginController {
	private Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private UserService userService;

	@Autowired
	protected AuthenticationManager authenticationManager;

	@RequestMapping("/loginJsonResp")
	@ResponseBody
	public Map<String, Object> showHome() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		// 从数据库中取出用户信息
		// UserInfo user = userService.selectByUserPhone(Long.parseLong(name));
		// // 判断用户是否存在
		// if(user == null) {
		//
		//
		//
		// userService.insertSelective(record);
		// }
		//

		logger.info("当前登陆用户：" + name);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", 1);
		return map;
	}

	@RequestMapping("/login/error")
	@ResponseBody
	public Map<String, Object> loginError(HttpServletRequest request, HttpServletResponse response) {
		AuthenticationException exception = (AuthenticationException) request.getSession()
				.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", 0);
		map.put("msg", "用户名或密码错误");
		return map;
	}

	@RequestMapping("/index")
	public ModelAndView index() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		logger.info("indez当前登陆用户：" + name);

		ModelAndView mv = new ModelAndView("page/index");
		mv.addObject("test", "hello world !!!index");
		mv.addObject("date", new Date());
		// mv.setViewName("forward:/login.html");
		return mv;
	}

	@RequestMapping("/login")
	public String showLogin() {
		logger.info("跳转至登录页");
		return "forward:/login.html";
	}

	@RequestMapping("/admin")
	@ResponseBody
	@PreAuthorize("hasPermission('/admin','r')")
	public String printAdminR() {
		return "如果你看见这句话，说明你访问/admin路径具有r权限";
	}

	@RequestMapping("/admin/c")
	@ResponseBody
	@PreAuthorize("hasPermission('/admin','c')")
	public String printAdminC() {
		return "如果你看见这句话，说明你访问/admin路径具有c权限";
	}

	// @RequestMapping("/admin")
	// @ResponseBody
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	// public String printAdmin() {
	// logger.info("如果你看见这句话，说明你有ROLE_ADMIN角色");
	//
	// return "page/test";
	// }

	@RequestMapping("/user")
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_USER')")
	public String printUser() {
		logger.info("如果你看见这句话，说明你有ROLE_USER角色");
		return "如果你看见这句话，说明你有ROLE_USER角色";
	}
}
