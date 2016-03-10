package com.dempe.ocean.client;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/26
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
public class NoAvailableClientException extends java.lang.Exception{

    private static final long serialVersionUID = -8195288970886184158L;

    /**
     * Creates a new exception.
     */
    public NoAvailableClientException() {
    }

    /**
     * Creates a new exception.
     */
    public NoAvailableClientException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new exception.
     */
    public NoAvailableClientException(String message) {
        super(message);
    }

    /**
     * Creates a new exception.
     */
    public NoAvailableClientException(Throwable cause) {
        super(cause);
    }
}

