package org.jumpmind.pos.util.model;

import java.io.Serializable;

public class ServiceResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object extension;

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
