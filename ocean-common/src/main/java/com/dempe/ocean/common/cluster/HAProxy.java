package com.dempe.ocean.common.cluster;

import com.dempe.ocean.common.NodeDetails;
import com.dempe.ocean.common.cluster.listener.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/23
 * Time: 20:27
 * To change this template use File | Settings | File Templates.
 */
public abstract class HAProxy<T> extends TimerTask {

    private final static Logger LOGGER = LoggerFactory.getLogger(HAProxy.class);


    private Timer timer;

    /**
     * 负载均衡策略
     * 默认 default
     * 轮叫调度Round Robin
     * 加权轮叫Weighted Round Robin
     * 哈希调度Hash
     */
    public enum Strategy {
        DEFAULT,
        RR,
        WRR,
        HASH
    }

    private Strategy strategy;

    private LoadBalance lb;


    /**
     * 可用的服务
     */
    protected List<NodeDetails> availServers = new CopyOnWriteArrayList<NodeDetails>();
    /**
     * 不可用的服务
     */
    protected List<NodeDetails> unAvailServers = new CopyOnWriteArrayList<NodeDetails>();

    private Set<HAListener> listeners = new HashSet<HAListener>();


    /**
     * @param strategy
     * @param period
     */
    public HAProxy(Strategy strategy, String name, long period) throws Exception {
        this.strategy = strategy;
        this.timer = new Timer();
        LOGGER.info("HALBProxy timer check started, period:{}", period);
        this.timer.schedule(this, 1000, period);
        initServerInstance(name);
    }


    /**
     * 获得位置
     *
     * @return
     */
    public int getIndex() {
        switch (this.strategy) {
            case DEFAULT:
                return 0;
            case RR:
                return this.lb.roundIndex();
            case WRR:
                return this.lb.roundIndexByWeight();
            case HASH:
                break;
        }
        return 0;
    }

    /**
     * hash key获得位置
     *
     * @param key
     * @return
     */
    public int getIndex(String key) {
        return this.lb.hashIndex(key);
    }

    /**
     * 可用转不可用
     *
     * @param serverInstance
     */
    private void toUnAvailable(NodeDetails serverInstance) {
        availServers.remove(serverInstance);
        this.unAvailServers.add(serverInstance);
        this.notifyHAListener(new ToAvailEvent(this));
    }

    /**
     * 不可用转可用
     *
     * @param serverInstance
     */
    private void toAvailable(NodeDetails serverInstance) {
        LOGGER.info("unAvailable server：{}", serverInstance.toString());
        if (this.availServers.size() == 0) { //如果可用节点的列表已空,则不需考虑负载的策略
            this.availServers.add(serverInstance);
        } else {
            if (this.strategy.equals(Strategy.DEFAULT)) {
                //如果待加入的unAvail服务节点is_default = true,则添加到队首; 否则,加入到队尾
                if (!serverInstance.isDefault()) { //非缺省节点,直接加到队尾
                    this.availServers.add(serverInstance);
                } else {//“缺省服务节点”,从不可用---->可用
                    if (availServers.contains(serverInstance)) {
                        availServers.remove(serverInstance);
                        availServers.add(0, serverInstance);
                    } else {
                        availServers.add(0, serverInstance);
                    }
                }
            } else {
                availServers.add(serverInstance);
            }
        }
        unAvailServers.remove(serverInstance);
        notifyHAListener(new ToUnAvailEvent(this));
    }

    /**
     * 获得可用的服务器信息
     *
     * @return 服务器信息
     */
    public NodeDetails getAvailServerInstance() {
        NodeDetails server = this.availServers.get(getIndex());
        server.incHits();
        return server;
    }

    /**
     * 获得可用的服务器信息
     *
     * @param key hash值
     * @return 服务器信息
     */
    public NodeDetails getAvailServerInstance(String key) {
        NodeDetails server = this.availServers.get(getIndex(key));
        server.incHits();
        return server;
    }


    /**
     * 初始化负载均衡器
     */
    private void initLoadBalance() {
        int[] weights = new int[this.availServers.size()];
        for (int i = 0; i < this.availServers.size(); i++) {
            NodeDetails serverInfo = this.availServers.get(i);
            weights[i] = serverInfo.getWeight();
        }
        if (Strategy.WRR == strategy) {
            this.lb = new LoadBalance(weights);
        } else {
            this.lb = new LoadBalance(this.availServers.size());
        }
    }

    /**
     * 初始化服务器配置，使用者调用
     *
     * @throws Exception
     */
    public List<NodeDetails> initServerInstance(String name) throws Exception {
        List<NodeDetails> serverInstances = initServerInstanceList(name);
        return initServerInstance(serverInstances);
    }

    /**
     * 初始化服务器配置，使用者调用
     *
     * @throws Exception
     */
    public List<NodeDetails> initServerInstance(List<NodeDetails> availServer) {
        if (availServer != null) {
            this.availServers.clear();
            availServers.addAll(availServer);
        }
        this.initLoadBalance();
        return this.availServers;
    }

    public void reloadServerInstance(List<NodeDetails> serverInstanceList) {
        for (NodeDetails serverInstance : serverInstanceList) {
            if (availServers.contains(serverInstance) || unAvailServers.contains(serverInstance)) {
                continue;
            }
            availServers.add(serverInstance);
        }

    }

    /**
     * 获得可用服务的client
     *
     * @param serverInstance
     * @return
     * @throws Exception
     */
    public T getClient(NodeDetails<T> serverInstance) {
        LOGGER.debug("getClient serverInfo:{}", serverInstance);
        T server = serverInstance.getClient();
        try {
            if (server == null) {
                server = this.createClient(serverInstance);
                serverInstance.setClient(server);
                LOGGER.info("getClient null so createClient:{}", server);
            }
        } catch (Exception e) {
            LOGGER.error("getClient error serverInfo:" + serverInstance, e);

        }
        if (server == null) {
            if (availServers.size() > 0) {
                // 删除当前节点
                toUnAvailable(serverInstance);
                initLoadBalance();
            }
            if (availServers.size() < 1) {
                checkToAvailable();
            }
            server = getClient(getAvailServerInstance());
        }
        return server;
    }


    /**
     * 轮询获得可用服务的client
     *
     * @return
     * @throws Exception
     */
    public T getClient() {
        if (availServers.size() < 1) {
            return null;
        }
        return getClient(getAvailServerInstance());
    }

    /**
     * hash获得可用服务的client
     *
     * @param key hash值
     * @return
     */
    public T getClientByHashKey(String key) {
        if (availServers.size() < 1) {
            return null;
        }
        return getClient(getAvailServerInstance(key));
    }

    /**
     * 直接更换可用节点的client
     *
     * @param client
     * @return
     */
    public T changeClient(T client) {
        if (client == null) {
            throw new IllegalArgumentException("client can't be null");
        }
        for (NodeDetails serverInstance : this.availServers) {
            if (client.equals(serverInstance.getClient())) {
                LOGGER.info("goto changeClient serverInstance:{}", serverInstance);
                return changeClient(serverInstance);
            }
        }
        return getClient();
    }

    /**
     * 直接更换可用节点的client
     *
     * @param serverInstance
     * @return
     */
    public T changeClient(NodeDetails serverInstance) {
        if (availServers.size() > 0) {
            toUnAvailable(serverInstance);
        }
        initLoadBalance();
        if (availServers.size() > 0) {
            return getClient();
        }
        return null;
    }


    /**
     * 定时检测服务是否可用
     */
    @Override
    public void run() {
        checkToAvailable();
    }

    private void checkToAvailable() {
        if (null != unAvailServers) {
            int availSize = availServers == null ? 0 : availServers.size();
            LOGGER.debug("HA check availSize:{}, unAvailableSize:{}", availSize, unAvailServers.size());
            for (NodeDetails serverInstance : unAvailServers) {
                T server = null;
                try {
                    server = getClient(serverInstance);
                } catch (Exception e) {
                    LOGGER.error("HA check error:" + serverInstance + " server:" + server, e);
                }
                if (server != null) {
                    toAvailable(serverInstance);
                    initLoadBalance();
                }
            }
        }
    }

    /**
     * @return
     */
    public String getAllAvailServerInstance() {
        return Arrays.toString(availServers.toArray());
    }

    /**
     * 个性初始化服务器信息，由子类实现
     *
     * @return
     * @throws Exception
     */
    public abstract List<NodeDetails> initServerInstanceList(String name) throws Exception;

    /**
     * 创建可用的服务器的client，由子类实现
     *
     * @param serverInstance
     * @return
     * @throws Exception
     */
    public abstract T createClient(NodeDetails serverInstance) throws Exception;

    public void addHAListener(HAListener listener) {
        listeners.add(listener);
    }

    public void removeHAListener(HAListener listener) {
        listeners.remove(listener);
    }

    public void notifyHAListener(HAEvent event) {
        for (HAListener listener : listeners) {
            try {
                if (event instanceof ToAvailEvent) {
                    if (listener instanceof ToAvailListener) {
                        listener.handleEvent(event);
                    } else {
                        continue;
                    }
                } else if (event instanceof ToUnAvailEvent) {
                    if (listener instanceof ToUnAvailListener) {
                        listener.handleEvent(event);
                    } else {
                        continue;
                    }
                } else if (event instanceof HAEvent) {
                    if (listener instanceof HAListener) {
                        listener.handleEvent(event);
                    } else {
                        continue;
                    }
                }
            } catch (Exception e) {
                LOGGER.error("notifyHAListener handleEvent error!", e);
            }
        }
    }


}
