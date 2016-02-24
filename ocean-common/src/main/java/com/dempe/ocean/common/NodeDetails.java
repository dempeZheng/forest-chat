package com.dempe.ocean.common;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/23
 * Time: 20:28
 * To change this template use File | Settings | File Templates.
 */
public class NodeDetails<T> {

    private String name;

    /**
     * 权重
     */
    private int weight;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 端口
     */
    private int port;

    /**
     * 命中
     */
    private long hits;

    private T client;

    /**
     * 是否是缺省的
     * true:缺省
     * false:非缺省
     */
    private boolean isDefault;

    public NodeDetails() {

    }

    /**
     * 构造方法
     *
     * @param ip   地址
     * @param port 端口
     */
    public NodeDetails(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }


    /**
     * 构造方法
     *
     * @param ip     地址
     * @param port   端口
     * @param weight 权重
     */
    public NodeDetails(String ip, int port, int weight) {
        this(ip, port);
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public long getHits() {
        return hits;
    }

    public void setHits(long hits) {
        this.hits = hits;
    }

    public void incHits() {
        this.hits++;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public T getClient() {
        return client;
    }

    public void setClient(T client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "NodeDetails{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", hits=" + hits +
                ", client=" + client +
                ", isDefault=" + isDefault +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeDetails that = (NodeDetails) o;
        if (that.getIp() == getIp() && that.getPort() == getPort()) {
            return true;
        }
        return false;
    }

}
