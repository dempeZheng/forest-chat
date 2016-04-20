package com.dempe.chat.connector.processor;

import com.dempe.chat.connector.NettyUtils;
import io.netty.channel.Channel;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/11
 * Time: 15:57
 * To change this template use File | Settings | File Templates.
 */
public class DisconnectMessageProcessor extends MessageProcessor {

    public void processDisconnect(Channel channel) throws InterruptedException {
        String clientID = NettyUtils.clientID(channel);
        boolean cleanSession = NettyUtils.cleanSession(channel);
        LOGGER.info("DISCONNECT client <{}> with clean session {}", clientID, cleanSession);

        m_clientIDs.remove(clientID);
        channel.close();

        //cleanup the will store
        m_willStore.remove(clientID);
        LOGGER.info("DISCONNECT client <{}> finished", clientID, cleanSession);
    }
}
