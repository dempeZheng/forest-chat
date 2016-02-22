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
package com.dempe.ocean.core.spi;

import com.dempe.ocean.core.BrokerConstants;
import com.dempe.ocean.core.ProtocolProcessor;
import com.dempe.ocean.core.config.IConfig;
import com.dempe.ocean.core.spi.impl.subscriptions.SubscriptionsStore;
import com.dempe.ocean.core.spi.persistence.MapDBPersistentStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Singleton class that orchestrate the execution of the protocol.
 * <p/>
 * It's main responsibility is instantiate the ProtocolProcessor.
 *
 * @author andrea
 */
public class SimpleMessaging {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleMessaging.class);

    private SubscriptionsStore subscriptions;

    private MapDBPersistentStore m_mapStorage;


    private static SimpleMessaging INSTANCE;

    private final ProtocolProcessor m_processor = new ProtocolProcessor();

    private SimpleMessaging() {
    }

    public static SimpleMessaging getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SimpleMessaging();
        }
        return INSTANCE;
    }

    /**
     * Initialize the processing part of the broker.
     *
     * @param props             the properties carrier where some props like port end host could be loaded.
     *                          For the full list check of configurable properties check moquette.conf file.
     * @param embeddedObservers a list of callbacks to be notified of certain events inside the broker.
     */
    public ProtocolProcessor init(IConfig props, List<?> embeddedObservers) {
        subscriptions = new SubscriptionsStore();

        m_mapStorage = new MapDBPersistentStore(props);
        m_mapStorage.initStore();
        IMessagesStore messagesStore = m_mapStorage.messagesStore();
        ISessionsStore sessionsStore = m_mapStorage.sessionsStore(messagesStore);


        subscriptions.init(sessionsStore);

        String configPath = System.getProperty("moquette.path", null);


        boolean allowAnonymous = Boolean.parseBoolean(props.getProperty(BrokerConstants.ALLOW_ANONYMOUS_PROPERTY_NAME, "true"));

        m_processor.init(subscriptions, messagesStore, sessionsStore);
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
