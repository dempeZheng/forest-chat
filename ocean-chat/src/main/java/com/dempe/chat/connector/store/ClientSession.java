package com.dempe.chat.connector.store;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/13
 * Time: 12:51
 * To change this template use File | Settings | File Templates.
 */
public class ClientSession {
    private final String clientID;
    private short nextMessageId = 1;
    private volatile boolean cleanSession;
    private boolean active = false;


    public ClientSession(String clientID, boolean cleanSession) {
        this.clientID = clientID;
        this.cleanSession = cleanSession;
    }


    public String getClientID() {
        return clientID;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public short getNextMessageId() {
        short rc = nextMessageId;
        nextMessageId++;
        if (nextMessageId == 0) {
            nextMessageId = 1;
        }
        return rc;
    }

}
