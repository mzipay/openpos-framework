package org.jumpmind.pos.management;

public class OpenposManagementException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public OpenposManagementException() {
        super();
    }

    public OpenposManagementException(String message) {
        super(message);
    }

    public OpenposManagementException(Throwable cause) {
        super(cause);
    }
    
    public OpenposManagementException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
