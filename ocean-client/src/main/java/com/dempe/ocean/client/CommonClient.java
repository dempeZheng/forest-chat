package com.dempe.ocean.client;

import com.dempe.ocean.common.NodeDetails;
import com.dempe.ocean.common.codec.DefaultEncoder;
import com.dempe.ocean.common.codec.MessageDecoder;
import com.dempe.ocean.common.protocol.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/2
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */
public class CommonClient {

    protected static final Logger LOGGER = LoggerFactory.getLogger(CommonClient.class);

    protected Bootstrap b;

    protected ChannelFuture f;

    protected Channel channel;

    protected EventLoopGroup group;
    protected Map<Integer, Context> contextMap = new ConcurrentHashMap<Integer, Context>();
    private DefaultEventExecutorGroup executorGroup;
    private String host;
    private int port;
    private long connectTimeout = 5000L;

    public CommonClient(NodeDetails nodeDetails) {
        this(nodeDetails.getIp(), nodeDetails.getPort());

    }


    public CommonClient(String host, int port) {
        this.host = host;
        this.port = port;
        init();
    }

    private void init() {
        b = new Bootstrap();
        group = new NioEventLoopGroup(4);
        executorGroup = new DefaultEventExecutorGroup(4,
                new DefaultThreadFactory("worker group"));
        b.group(group)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        initClientChannel(ch);
                    }
                });

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    closeSync();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }));
        connect(host, port);
    }

    public void initClientChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("RequestEncoder", new DefaultEncoder())
                .addLast("ResponseDecoder", new MessageDecoder())
                .addLast("ClientHandler", new ChannelHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        Integer id;
                        Message resp = (Message) msg;
                        id = resp.getMessageID();
                        Context context = contextMap.remove(id);
                        if (context == null) {
                            LOGGER.debug("messageID:{}, take Context null", id);
                            return;
                        }
                        context.cb.onReceive(resp);
                    }
                });
    }

    public void connect(final String host, final int port) {
        try {
            f = b.connect(host, port).sync();
            channel = f.channel();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void closeSync() throws IOException {
        try {
            f.channel().close().sync();
            group.shutdownGracefully();

        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void close() {
        if (isConnected()) {
            f.channel().close();
        }
    }

    public boolean reconnect() throws Exception {
        close();
        LOGGER.info("start reconnect to server.");
        f = b.connect(host, port);// 异步建立长连接
        f.get(connectTimeout, TimeUnit.MILLISECONDS); // 最多等待5秒，如连接建立成功立即返回
        LOGGER.info("end reconnect to server result:" + isConnected());
        return isConnected();
    }

    public boolean isConnected() {
        return f != null && f.channel().isActive();
    }

    public void writeAndFlush(Object request) throws Exception {
        if (!isConnected()) {
            reconnect();
        }
        f.channel().writeAndFlush(request);
    }

    public static class Context {
        protected final Message request;
        public final Callback cb;
        protected final short id;

        public Context(int id, Message request, Callback cb) {
            this.id = (short) id;
            this.cb = cb;
            this.request = request;
        }
    }


}
