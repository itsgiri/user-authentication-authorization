package com.user.auth.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.user.auth.constants.CommonConstants;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleExceptions(Exception err, WebRequest webRequest) {
    	
    	String message = err.getMessage();
    	
        ExceptionResponse response = new ExceptionResponse(CommonConstants.STATUS_FAILURE,message);
        response.setCaughtTime(LocalDateTime.now());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception err, WebRequest webRequest) {
    	
    	String message = err.getMessage();
    	
        ExceptionResponse response = new ExceptionResponse(CommonConstants.STATUS_FAILURE,message);
        response.setCaughtTime(LocalDateTime.now());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}