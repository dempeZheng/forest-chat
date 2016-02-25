package com.dempe.ocean.bus.sample;


import com.dempe.ocean.bus.BusServer;
import com.dempe.ocean.common.register.NameDiscoveryService;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/23
 * Time: 10:12
 * To change this template use File | Settings | File Templates.
 */
public class SampleServer {

    public static void main(String[] args) throws Exception {

        BusServer server = new BusServer();
        NameDiscoveryService forestNameService = new NameDiscoveryService();
        forestNameService.start();
        forestNameService.register();
        server.startServer();
    }
}
