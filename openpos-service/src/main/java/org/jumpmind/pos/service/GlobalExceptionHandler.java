package org.jumpmind.pos.service;

import org.jumpmind.pos.util.model.ErrorResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ErrorResult handleErrors(Throwable ex) {
        return new ErrorResult(ex.getMessage(), ex);
    }
}
