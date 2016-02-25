package com.dempe.ocean.common;

import org.aeonbits.owner.Config;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/23
 * Time: 12:44
 * To change this template use File | Settings | File Templates.
 */
@Config.Sources("classpath:application.properties")
public interface OceanConfig extends Config {

    @Key("host")
    @DefaultValue("localhost")
    public String host();

    @Key("port")
    @DefaultValue("1883")
    public int port();

    @Key("websocket.port")
    @DefaultValue("8080")
    public int webSocketPort();

    @Key("websocket.host")
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

    String getInterceptHandler();


    //  authenticator_class
    @DefaultValue("")
    String authenticatorClass();


    //password_file
    @DefaultValue("")
    String pwdFile();

    @DefaultValue("true")
    boolean allowAnonymous();

    String aclFile();

    @DefaultValue("0")
    int sslPort();

    @DefaultValue("0")
    int secureWebsocketPort();


    //**************************************
    // security==========================

    String jksPath();
    String keyStorePassword();

    String keyManagerPassword();
}
