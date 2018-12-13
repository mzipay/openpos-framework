package org.jumpmind.pos.service;

import org.jumpmind.pos.util.model.ErrorResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ErrorResult handleErrors(Throwable ex, WebRequest request) {
        logger.warn("Web request failed.  " + request.getDescription(true), ex);
        return new ErrorResult(ex.getMessage(), ex);
    }
}
