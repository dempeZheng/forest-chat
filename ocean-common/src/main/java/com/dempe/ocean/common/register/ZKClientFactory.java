package com.dempe.ocean.common.register;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/1/29
 * Time: 16:58
 * To change this template use File | Settings | File Templates.
 */
public class ZKClientFactory {

    public static CuratorFramework getZKClient() {
        return CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000, 3));
    }
}
