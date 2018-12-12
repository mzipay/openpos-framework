package org.jumpmind.pos.util.model;

import java.io.Serializable;

public class ServiceResult implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Result {
        SUCCESS, FAILURE, NOT_FOUND, UNKNOWN
    };

    private Result resultStatus = Result.UNKNOWN;
    private String resultMessage = "";
    private Object extension;
    private Throwable throwable;

    public boolean isSuccess() {
        return resultStatus == Result.SUCCESS;
    }

    public Result getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(Result resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage, Object... args) {
        if (this.resultMessage != null) {
           this.resultMessage = String.format(resultMessage, args);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T ext() {
        return (T) extension;
    }

    @SuppressWarnings("unchecked")
    public <T> T ext(Class<T> type) {
        return (T) extension;
    }

    public Object getExtension() {
        return extension;
    }

    public void setExtension(Object extension) {
        this.extension = extension;
    }

    public void setThrowable(Throwable ex) {
        this.throwable = ex;
    }

    public Throwable getThrowable() {
        return throwable;
    }

}
