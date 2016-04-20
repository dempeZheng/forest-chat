package com.dempe.ocean.rpc.transport.compress;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/15
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */
public enum CompressType {

    /**
     * No compress
     */
    NO(0),

    /**
     * Snappy compress
     */
    Snappy(1),

    /**
     * GZIP compress
     */
    GZIP(2);

    private final int value;

    CompressType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }


}
