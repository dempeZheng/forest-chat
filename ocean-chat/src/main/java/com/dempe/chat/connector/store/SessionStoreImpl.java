package com.dempe.chat.connector.store;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/13
 * Time: 12:56
 * To change this template use File | Settings | File Templates.
 */
public class SessionStoreImpl implements ISessionStore {

    private final static Map<String, ClientSession> m_sessionStore = Maps.newConcurrentMap();

    @Override
    public ClientSession sessionForClient(String clientID) {
        ClientSession clientSession = m_sessionStore.get(clientID);
        if (clientSession == null) {
            clientSession = new ClientSession(clientID, false);
        }
        return clientSession;
    }
}
