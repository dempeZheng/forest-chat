package com.dempe.ocean.core.spi.persistence;

import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/24
 * Time: 13:12
 * To change this template use File | Settings | File Templates.
 */
public class UidSessionStore {

    private final static Logger LOGGER = LoggerFactory.getLogger(UidSessionStore.class);

    private final static Map<String, Channel> uidSessionMap = Maps.newConcurrentMap();

    public static Channel getSessionByUid(String uid) {
        return uidSessionMap.get(uid);

    }

    public static void put(String uid, Channel session) {
        uidSessionMap.put(uid, session);
    }

    public static Channel remove(String uid) {
        LOGGER.info("--------------->>>>>>>>>>>>>>>>>>>>>>>>>> remove uid {} channel", uid);
        return uidSessionMap.remove(uid);
    }
}
