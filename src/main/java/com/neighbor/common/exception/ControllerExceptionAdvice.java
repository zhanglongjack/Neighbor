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
    	
		String msg = ex.getMessage();
		String msgs[] = msg.split(":");
		result.setErrorMessage(msgs.length==2 ? msgs[1]:msg); 
    	
    	logger.error("统一异常处理:"+JSON.toJSONString(result));
    	logger.error(ex.getMessage(),ex);
    	return result;
    }
    
    /**
     * spring 验证框架异常
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseResult constraintViolationHandler(Exception ex) {
    	
    	ResponseResult result = new ResponseResult();
    	result.setErrorCode(ErrorCodeDesc.failed.getValue()); 
		String msg = ex.getMessage();
		String msgs[] = msg.split(":");
		result.setErrorMessage(msgs.length==2 ? msgs[1]:msg); 
    	
    	logger.error("统一验证框架异常处理:"+JSON.toJSONString(result));
    	logger.error(ex.getMessage(),ex);
    	return result;
    }
    
    /**
     * 全局参数检查异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ParamsCheckException.class)
    public ResponseResult paramsCheckHandler(ParamsCheckException ex) {
    	
    	ResponseResult result = new ResponseResult();
    	result.setErrorCode(ex.getErrorCode());
    	result.setErrorMessage(ex.getErrorMessage());  
    	logger.error("统一参数检查异常处理:"+JSON.toJSONString(result));
    	logger.error(ex.getMessage(),ex);
    	return result;
    }
 
}
