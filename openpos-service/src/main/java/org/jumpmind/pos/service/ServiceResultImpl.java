package org.jumpmind.pos.service;

public class ServiceResultImpl {
    
    private String resultStatus = "";
    private String resultMessage = "";
    private Object extension;
    
    public String getResultStatus() {
        return resultStatus;
    }
    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }
    public String getResultMessage() {
        return resultMessage;
    }
    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
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

}
