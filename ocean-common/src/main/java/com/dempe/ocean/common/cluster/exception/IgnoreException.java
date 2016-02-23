package com.dempe.ocean.common.cluster.exception;


/**
 * 类说明：不需要高可用动态代理，处理的异常;
 *
 * @version:v1.00
 */
public class IgnoreException extends RuntimeException {
    private static final long serialVersionUID = 4801974815987722102L;

    public IgnoreException(String msg, Exception e) {
        super("msg:" + msg, e);
    }

    public IgnoreException(String msg) {
        super("msg:" + msg);
    }
}