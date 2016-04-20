package com.dempe.ocean.rpc.core;

import com.dempe.ocean.rpc.RPCService;
import com.dempe.ocean.rpc.RPCMethod;
import com.dempe.ocean.rpc.utils.PackageUtils;
import com.dempe.ocean.rpc.utils.PathUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/15
 * Time: 11:14
 * To change this template use File | Settings | File Templates.
 */
public class ServiceMapping {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceMapping.class);

    public Map<String, ActionMethod> mapping = new ConcurrentHashMap<String, ActionMethod>();

    private ApplicationContext context;

    public ServiceMapping(String scanPKG, ApplicationContext context) {
        this.context = context;
        initMapping(scanPKG);
    }

    /**
     * 扫描packet下所有的Controller映射，初始化mapping
     * 默认规则为类名/方法名
     */
    public void initMapping(String scanPKG) {
        LOGGER.info("handles begin initiating");
        List<String> packages = Lists.newArrayList();
        packages.add(scanPKG);
        LOGGER.info("scanned packages :{} ", packages);
        for (String scanPackage : packages) {
            LOGGER.info("begin get classes from package {} : ", scanPackage);
            String[] classNames = PackageUtils.findClassesInPackage(scanPackage + ".*"); // 目录下通配
            for (String className : classNames) {
                try {
                    Class<?> actionClass = Class.forName(className);
                    RPCService exporter = actionClass.getAnnotation(RPCService.class);
                    if (exporter == null) {
                        continue;
                    }
                    String serviceName = exporter.serviceName();
                    if (StringUtils.isBlank(serviceName)) {
                        serviceName = actionClass.getSimpleName();
                    }
                    LOGGER.info("registering action  :{} ", serviceName);
                    Method[] declaredMethods = actionClass.getDeclaredMethods();
                    for (Method method : declaredMethods) {
                        RPCMethod refs = method.getAnnotation(RPCMethod.class);
                        if (refs != null) {
                            String methodName = String.valueOf(refs.methodName());
                            if (StringUtils.isBlank(methodName)) {
                                methodName = method.getName();
                            }
                            String path = PathUtil.buildPath(serviceName, methodName);
                            if (mapping.containsKey(path)) {
                                LOGGER.warn("Method:{} declares duplicated jsonURI:{}, previous one will be overwritten", method, path);
                            }
                            makeAccessible(method);
                            /**
                             * 从spring ioc容器中获取相应的bean
                             */
                            Object target = context.getBean(actionClass);
                            ActionMethod actionMethod = new ActionMethod(target, method);
                            LOGGER.info("[REQUEST MAPPING] = {}, jsonURI = {}", serviceName, path);
                            mapping.put(path, actionMethod);
                        }
                    }

                } catch (ClassNotFoundException e) {
                    LOGGER.error("FAIL to initiate handle instance", e);
                } catch (Exception e) {
                    LOGGER.error("FAIL to initiate handle instance", e);
                }
            }
        }
        LOGGER.info("Handles  Initialization successfully");
    }

    public ActionMethod tack(String uri) {
        return mapping.get(uri);
    }


    protected void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
                && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }
}
