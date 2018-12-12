package org.jumpmind.pos.util.model;

import java.io.Serializable;

public class ErrorResult implements Serializable {

    private static final long serialVersionUID = 1L;
    
    String message;

    Throwable throwable;
    
    public ErrorResult() {
    }
    
    public ErrorResult(String message, Throwable throwable) {
        this.message = message;
        this.throwable = throwable;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Throwable getThrowable() {
        return throwable;
    }
    
    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
    
}
