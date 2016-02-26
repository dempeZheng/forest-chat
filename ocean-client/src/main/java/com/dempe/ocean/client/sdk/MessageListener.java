package com.dempe.ocean.client.sdk;

import com.dempe.ocean.common.protocol.Request;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/26
 * Time: 18:26
 * To change this template use File | Settings | File Templates.
 */
public interface MessageListener {

    public void onPublish(Request request);

}
