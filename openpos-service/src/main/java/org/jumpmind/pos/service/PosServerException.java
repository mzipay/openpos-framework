package org.jumpmind.pos.service;


public class PosServerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PosServerException() {
        super();
    }

    public PosServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public PosServerException(String message, Object... args) {
        super(String.format(message, args));
    }

    public PosServerException(Throwable cause) {
        super(cause);
    }
}
