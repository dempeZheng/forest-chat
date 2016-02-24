package com.dempe.ocean.common.codec;


import com.dempe.ocean.common.pack.Marshallable;
import com.dempe.ocean.common.pack.Pack;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/3
 * Time: 11:35
 * To change this template use File | Settings | File Templates.
 */
public class DefaultEncoder extends AbstractEncoder {
    @Override
    public Pack encode(Marshallable request) {
        return request.marshal(new Pack());
    }
}
