package com.dempe.ocean.common.utils;


import com.dempe.ocean.common.EnvEnum;
import com.dempe.ocean.common.OceanConfig;
import org.aeonbits.owner.ConfigFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/1/14
 * Time: 12:45
 * To change this template use File | Settings | File Templates.
 */
public class DefConfigFactory {

    private DefConfigFactory() {}

    public static OceanConfig createUATConfig() {
        return createConfig(EnvEnum.UAT.getEnv());
    }

    public static OceanConfig createDEVConfig() {
        return createConfig(EnvEnum.DEV.getEnv());
    }

    public static OceanConfig createPRODConfig() {
        return createConfig(EnvEnum.PROD.getEnv());
    }

    public static OceanConfig createConfig(String env) {
        Map myVars = new HashMap();
        myVars.put("env", env);
        System.setProperty("env", env);
        return ConfigFactory.create(OceanConfig.class, myVars);
    }


}
