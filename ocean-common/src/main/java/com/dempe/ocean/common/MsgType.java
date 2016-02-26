package com.dempe.ocean.common;


public enum MsgType {

    UNICAST((short) 1),
    MUlTIUSERS((short) 2),
    BCSUBCH((short) 3),
    BCTOPCH((short) 4),
    AREA_MULTICAST((short) 5),;
    private short value;

    private MsgType(short value) {
        this.value = value;
    }

    public short getValue() {
        return this.value;
    }
}
