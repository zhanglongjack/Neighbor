package com.neighbor.common.exception;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.users.controller.LoginController;
import com.neighbor.common.util.ResponseResult;
 
/**
 * controller 增强器
 *
 * @author sam
 * @since 2017/7/17
 */
@ControllerAdvice
public class ControllerExceptionAdvice {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
 
    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseResult errorHandler(Exception ex) {
    	
    	ResponseResult result = new ResponseResult();
    	result.setErrorCode(ErrorCodeDesc.failed.getValue());
    	result.setErrorMessage(ex.getMessage()); 
    	
    	if(ex instanceof ConstraintViolationException){
    		String msg = ex.getMessage();
    		String msgs[] = msg.split(":");
    		result.setErrorMessage(msgs.length==2 ? msgs[1]:msg); 
    	}
    	
    	logger.info("统一异常处理:"+JSON.toJSONString(result));
    	logger.error(ex.getMessage(),ex);
    	return result;
    }
 
}
