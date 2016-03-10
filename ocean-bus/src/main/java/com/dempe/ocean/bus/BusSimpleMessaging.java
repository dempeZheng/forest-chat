/*
 * Copyright (c) 2012-2015 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * The Apache License v2.0 is available at
 * http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package com.dempe.ocean.bus;


import com.dempe.ocean.client.service.IMServiceClient;
import com.dempe.ocean.common.OceanConfig;
import com.dempe.ocean.common.R;
import com.dempe.ocean.core.BrokerInterceptor;
import com.dempe.ocean.core.interception.InterceptHandler;
import com.dempe.ocean.core.spi.IMessagesStore;
import com.dempe.ocean.core.spi.ISessionsStore;
import com.dempe.ocean.core.spi.impl.subscriptions.SubscriptionsStore;
import com.dempe.ocean.core.spi.persistence.MapDBPersistentStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class that orchestrate the execution of the protocol.
 * <p/>
 * It's main responsibility is instantiate the ProtocolProcessor.
 *
 * @author andrea
 */
public class BusSimpleMessaging {

    private static final Logger LOG = LoggerFactory.getLogger(BusSimpleMessaging.class);

    private SubscriptionsStore subscriptions;

    private MapDBPersistentStore m_mapStorage;

    private BrokerInterceptor m_interceptor;

    private static BusSimpleMessaging INSTANCE;

    private final static BusProtocolProcessor m_processor = new BusProtocolProcessor();

    private BusSimpleMessaging() {
    }

    public static BusSimpleMessaging getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BusSimpleMessaging();
        }
        return INSTANCE;
    }


    public static BusProtocolProcessor getProtocolProcessor() {
        return m_processor;
    }

    /**
     * Initialize the processing part of the broker.
     *
     * @param embeddedObservers a list of callbacks to be notified of certain events inside the broker.
     */
    public BusProtocolProcessor init(OceanConfig config, List<? extends InterceptHandler> embeddedObservers) throws Exception {
        subscriptions = new SubscriptionsStore();

        m_mapStorage = new MapDBPersistentStore(config);
        m_mapStorage.initStore();
        IMessagesStore messagesStore = m_mapStorage.messagesStore();
        ISessionsStore sessionsStore = m_mapStorage.sessionsStore(messagesStore);

        List<InterceptHandler> observers = new ArrayList<>(embeddedObservers);
        String interceptorClassName = config.getInterceptHandler();
        if (interceptorClassName != null && !interceptorClassName.isEmpty()) {
            try {
                InterceptHandler handler = Class.forName(interceptorClassName).asSubclass(InterceptHandler.class).newInstance();
                observers.add(handler);
            } catch (Throwable ex) {
                LOG.error("Can't load the intercept handler {}", ex);
            }
        }
        m_interceptor = new BrokerInterceptor(observers);

        subscriptions.init(sessionsStore);

        boolean allowAnonymous = config.allowAnonymous();
        IMServiceClient imServiceClient = new IMServiceClient(R.FOREST_LEAF_NAME);
        m_processor.init(subscriptions, messagesStore, sessionsStore, allowAnonymous, imServiceClient, m_interceptor);
        return m_processor;
    }

    private Object loadClass(String className, Class<?> cls) {
        Object instance = null;
        try {
            Class<?> clazz = Class.forName(className);

            // check if method getInstance exists
            Method method = clazz.getMethod("getInstance", new Class[]{});
            try {
                instance = method.invoke(null, new Object[]{});
            } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException ex) {
                LOG.error(null, ex);
                throw new RuntimeException("Cannot call method " + className + ".getInstance", ex);
            }
        } catch (NoSuchMethodException nsmex) {
            try {
                instance = this.getClass().getClassLoader()
                        .loadClass(className)
                        .asSubclass(cls)
                        .newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
                LOG.error(null, ex);
                throw new RuntimeException("Cannot load custom authenticator class " + className, ex);
            }
        } catch (ClassNotFoundException ex) {
            LOG.error(null, ex);
            throw new RuntimeException("Class " + className + " not found", ex);
        } catch (SecurityException ex) {
            LOG.error(null, ex);
            throw new RuntimeException("Cannot call method " + className + ".getInstance", ex);
        }

        return instance;
    }

    public void shutdown() {
        this.m_mapStorage.close();
    }
}
