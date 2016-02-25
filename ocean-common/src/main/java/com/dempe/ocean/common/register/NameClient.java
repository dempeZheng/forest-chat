package com.dempe.ocean.common.register;

import com.dempe.ocean.common.NodeDetails;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/1/29
 * Time: 16:43
 * To change this template use File | Settings | File Templates.
 */
public class NameClient implements Closeable {
    private final ServiceDiscovery<NodeDetails> serviceDiscovery;
    private final ServiceInstance<NodeDetails> thisInstance;

    public NameClient(CuratorFramework client, String path, NodeDetails nodeDetails) throws Exception {
        // in a real application, you'd have a convention of some kind for the
        // URI layout
        UriSpec uriSpec = new UriSpec("{scheme}://{address}:{port}");
        thisInstance = ServiceInstance.<NodeDetails>builder().name(nodeDetails.getName()).payload(nodeDetails)
                .port(nodeDetails.getPort()) // in a real application,
                .address(nodeDetails.getIp())
                .uriSpec(uriSpec).build();
        // if you mark your payload class with @JsonRootName the provided
        // JsonInstanceSerializer will work
        JsonInstanceSerializer<NodeDetails> serializer = new JsonInstanceSerializer<NodeDetails>(NodeDetails.class);
        serviceDiscovery = ServiceDiscoveryBuilder.builder(NodeDetails.class).client(client).basePath(path).serializer(serializer)
                .thisInstance(thisInstance).build();
    }

    public ServiceInstance<NodeDetails> getThisInstance() {
        return thisInstance;
    }

    public void start() throws Exception {
        serviceDiscovery.start();
    }

    @Override
    public void close() throws IOException {
        CloseableUtils.closeQuietly(serviceDiscovery);
    }

}
