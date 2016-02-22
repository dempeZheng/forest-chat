package com.dempe.ocean.client;

import com.dempe.ocean.common.protocol.Request;
import com.dempe.ocean.common.protocol.Response;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/22
 * Time: 17:52
 * To change this template use File | Settings | File Templates.
 */
public interface Client {

    /**
     * 用户进入直播间
     *
     * @param uid
     * @param topSid
     * @param subSid
     */
    public Response subscribe(Long uid, Long topSid, Long subSid);

    /**
     * 用户退出直播间
     *
     * @param uid
     * @param topSid
     * @param subSid
     */
    public void unSubscribe(Long uid, Long topSid, Long subSid);

    /**
     * 直播间业务消息单播
     *
     * @param uid
     * @param topSid
     * @param subSid
     * @param request
     * @return
     */
    public Response publish(Long uid, Long topSid, Long subSid, Request request);


    /**
     * 直播间业务消息广播
     *
     * @param topSid
     * @param subSid
     * @param request
     * @return
     */
    public Response publishBC(Long topSid, Long subSid, Request request);


    /**
     * 直播间业务消息多播
     *
     * @param uidList
     * @param topSid
     * @param subSid
     * @param request
     * @return
     */
    public Response publishMultiBC(List<Long> uidList, Long topSid, Long subSid, Request request);


}
