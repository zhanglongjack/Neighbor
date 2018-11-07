package com.neighbor.common.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.neighbor.common.util.ResponseResult;

@Aspect
@Component
public class ControllerAspect { 

	@Pointcut("execution(* com.neighbor.app.*.controller..*.*(..))")
	public void addAdvice() {
	}
	
	
	/**
	 * @Before注解声明一个建言，此建言直接使用拦截规则作为参数
	 * @param joinPoint
	 */
	@AfterReturning(returning="result" , pointcut="addAdvice()")
	public void afterReturn(JoinPoint joinPoint,ResponseResult result) { 
		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		result.setServiceURL((String)request.getParameter("serviceURL"));
		
		System.out.println("return之后处理: " + result);
	}
}
