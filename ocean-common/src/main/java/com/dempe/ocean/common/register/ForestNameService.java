package com.dempe.ocean.common.register;

import com.dempe.ocean.common.NodeDetails;
import com.google.common.collect.Lists;
import org.aeonbits.owner.ConfigFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.*;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.apache.curator.x.discovery.details.ServiceCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 进程名称服务
 * User: Dempe
 * Date: 2016/1/29
 * Time: 16:36
 * To change this template use File | Settings | File Templates.
 */
public class ForestNameService implements NameService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ForestNameService.class);

    private static final String PATH = "/ocean/discovery";

    private ServiceDiscovery<NodeDetails> serviceDiscovery;

    private CuratorFramework zkClient;

    private ForestNameClient nameClient;

    private ServiceCache serviceCache;


    public ForestNameService() throws Exception {
        init();
        addCloseShutDownHook();
    }

    public void start() throws Exception {
        this.serviceCache.start();
        this.serviceDiscovery.start();
    }

    public void init() throws Exception {
        this.zkClient = ZKClientFactory.getZKClient();
        zkClient.start();
        JsonInstanceSerializer<NodeDetails> serializer = new JsonInstanceSerializer<NodeDetails>(NodeDetails.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(NodeDetails.class).client(zkClient).basePath(PATH).serializer(serializer).build();
        ServiceCacheBuilder<NodeDetails> nodeDetailsServiceCacheBuilder = serviceDiscovery.serviceCacheBuilder();
        this.serviceCache = nodeDetailsServiceCacheBuilder.name("nameCache").build();
    }

    public void addServiceCacheListener(ServiceCacheListener listener) {
        serviceCache.addListener(listener);
    }

    @Override
    public void register(NodeDetails node) throws Exception {
        nameClient = new ForestNameClient(zkClient, PATH, node);
        nameClient.start();
    }

    @Override
    public void register() throws Exception {
        NodeDetails nodeDetails = getNodeByCfg();
        register(nodeDetails);
    }

    @Override
    public List<Collection<ServiceInstance<NodeDetails>>> list() throws Exception {
        Collection<String> serviceNames = serviceDiscovery.queryForNames();
        List<Collection<ServiceInstance<NodeDetails>>> list = Lists.newArrayList();
        for (String serviceName : serviceNames) {
            Collection<ServiceInstance<NodeDetails>> instances = serviceDiscovery.queryForInstances(serviceName);
            list.add(instances);
        }
        return list;
    }

    @Override
    public Collection<ServiceInstance<NodeDetails>> listByName(String name) throws Exception {
        return serviceDiscovery.queryForInstances(name);
    }

    public void addCloseShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }));
    }

    public void close() throws IOException {
        if (nameClient != null) {
            nameClient.close();
        }
        if (serviceDiscovery != null) {
            serviceDiscovery.close();
        }
        if (serviceCache != null) {
//            serviceCache.close();
        }


    }

    private NodeDetails getNodeByCfg() {
        NameConfig cfg = ConfigFactory.create(NameConfig.class);
        NodeDetails nodeDetails = new NodeDetails();
        nodeDetails.setName(cfg.name());
        nodeDetails.setIp("localhost");
        nodeDetails.setPort(cfg.port());
        LOGGER.info(">>>>>>>>>>>>>>>>>>>nodeDetails:{}", nodeDetails);
        return nodeDetails;
    }
}
