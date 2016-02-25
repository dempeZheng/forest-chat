package com.dempe.ocean.node.simulator;

import com.dempe.ocean.common.NodeDetails;
import com.dempe.ocean.common.register.NameDiscoveryService;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/3
 * Time: 13:23
 * To change this template use File | Settings | File Templates.
 */
public class NameServiceSimulator {
    public static void main(String[] args) throws Exception {
        NameDiscoveryService nameService = new NameDiscoveryService();
        nameService.start();
        List<Collection<ServiceInstance<NodeDetails>>> list = nameService.list();
        for (Collection<ServiceInstance<NodeDetails>> serviceInstances : list) {
            for (ServiceInstance<NodeDetails> serviceInstance : serviceInstances) {
                System.out.println(serviceInstance.getName());
            }
        }
    }
}
