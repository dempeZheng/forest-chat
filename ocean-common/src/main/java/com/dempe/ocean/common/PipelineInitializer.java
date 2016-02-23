package com.dempe.ocean.common;

import io.netty.channel.ChannelPipeline;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/23
 * Time: 13:03
 * To change this template use File | Settings | File Templates.
 */
public abstract class PipelineInitializer {
    public abstract void init(ChannelPipeline pipeline) throws Exception;
}
