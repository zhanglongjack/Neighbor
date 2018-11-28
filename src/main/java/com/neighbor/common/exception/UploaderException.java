package com.neighbor.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.INTERNAL_SERVER_ERROR,reason="Uploader file error !!!")
public class UploaderException extends Exception {
    private static final long serialVersionUID = 1L;

}
