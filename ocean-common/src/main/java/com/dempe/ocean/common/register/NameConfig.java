package com.dempe.ocean.common.register;

import com.dempe.ocean.common.R;
import org.aeonbits.owner.Config;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/1/29
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */
@Config.Sources("classpath:name.properties")
public interface NameConfig extends Config {

    @Key("name")
    @DefaultValue(R.FOREST_LEAF_NAME)
    String name();

    @Key("port")
    @DefaultValue("8888")
    int port();


}
