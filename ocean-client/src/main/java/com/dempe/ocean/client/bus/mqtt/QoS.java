package com.dempe.ocean.client.bus.mqtt;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/24
 * Time: 11:03
 * To change this template use File | Settings | File Templates.
 */
public enum QoS {
    AT_MOST_ONCE,
    AT_LEAST_ONCE,
    EXACTLY_ONCE,
    UNICAST
}
