package com.dempe.chat.connector.store;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/13
 * Time: 12:56
 * To change this template use File | Settings | File Templates.
 */
public interface ISessionStore {

    public ClientSession sessionForClient(String clientID);
}
