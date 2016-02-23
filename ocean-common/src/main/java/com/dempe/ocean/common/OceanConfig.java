package com.dempe.ocean.common;

import org.aeonbits.owner.Config;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/23
 * Time: 12:44
 * To change this template use File | Settings | File Templates.
 */
public interface OceanConfig extends Config {

    @DefaultValue("localhost")
    public String host();

    @DefaultValue("1883")
    public int port();

    @DefaultValue("8080")
    public int webSocketPort();

    @DefaultValue("localhost")
    public String webSocketHost();

    @DefaultValue("persistent_store")
    public String persistentStore();

    @DefaultValue("30")
    public int autoSaveInterval();



    @DefaultValue("true")
    boolean tcpNoDelay();

    @DefaultValue("true")
    boolean soKeepAlive();

    @Key("common.package")
    @DefaultValue("com.dempe.ocean.node")
    String getPackageName();

}
