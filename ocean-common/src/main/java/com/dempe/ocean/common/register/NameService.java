package com.dempe.ocean.common.register;

import com.dempe.ocean.common.NodeDetails;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/1/29
 * Time: 16:37
 * To change this template use File | Settings | File Templates.
 */
public interface NameService {

    public void register(NodeDetails node) throws Exception;

    public void register() throws Exception;

    public List<Collection<ServiceInstance<NodeDetails>>> list() throws Exception;

    public Collection<ServiceInstance<NodeDetails>> listByName(String name) throws Exception;

    //public NameClient subscribe(String namePath);
}
