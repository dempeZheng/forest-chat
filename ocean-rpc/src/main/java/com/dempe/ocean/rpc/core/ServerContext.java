package com.dempe.ocean.rpc.core;


import com.dempe.ocean.rpc.AppConfig;
import com.dempe.ocean.rpc.transport.protocol.PacketData;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.context.ApplicationContext;

/**
 * 进程执行上下文环境
 * User: zhengdaxia
 * Date: 15/10/17
 * Time: 上午10:45
 * To change this template use File | Settings | File Templates.
 */
public class ServerContext {

    static final ThreadLocal<Context> localContext = new ThreadLocal<Context>();
    public ServiceMapping mapping;
    private ApplicationContext context;
    private AppConfig config;

    public ServerContext(AppConfig config, ApplicationContext context) {
        this.context = context;
        this.config = config;
        this.mapping = new ServiceMapping(config.getPackageName(), context);

    }

    /**
     * 获取业务执行的Request所有信息
     *
     * @return
     */
    public static PacketData getRequest() {
        return getContext().getRequest();
    }

    static Context getContext() {
        Context context = localContext.get();
        if (context == null) {
            throw new RuntimeException("Please apply " + ServerContext.class.getName()
                    + " to any request which uses servlet scopes.");
        }
        return context;
    }


    /**
     * 将上下文环境暴露给业务使用方
     * 将方法执行的上线文放入ThreadLocal中，便于业务逻辑中需要时获取对应的执行环境
     *
     * @param request 请求消息
     * @param ctx     netty执行上下文环境
     */
    public void setLocalContext(PacketData request, ChannelHandlerContext ctx) {
        localContext.set(new Context(request, ctx));
    }

    public void removeLocalContext() {
        localContext.remove();
    }

    public ActionMethod tackAction(String uri) {
        return mapping.tack(uri);
    }

    static class Context {

        final PacketData request;

        final ChannelHandlerContext response;

        Context(PacketData request, ChannelHandlerContext response) {
            this.request = request;
            this.response = response;
        }

        PacketData getRequest() {
            return request;
        }

        ChannelHandlerContext getResponse() {
            return response;
        }
    }
}
