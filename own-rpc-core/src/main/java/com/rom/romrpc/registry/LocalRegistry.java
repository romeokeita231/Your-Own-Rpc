package com.rom.romrpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地注册中心
 * */
public class LocalRegistry {
    /**
     * 注册信息存储
     */
    private static final Map<String,Class<?>> map = new ConcurrentHashMap<>();

    /**
     * 注册服务
     * @param serviceName 服务名称
     * @param implClass 服务实现类
    */
    public static void register(String serviceName,Class<?> implClass){
        map.put(serviceName,implClass);
        System.out.println("服务注册成功: " + serviceName + " -> " + implClass.getName());
    }

    /**
     * 获取服务
     * @param serviceName 服务名称
     * @return 服务实现类
     */
    public static Class<?> get(String serviceName){
        return map.get(serviceName);
    }

    /**
     * 删除服务
     * @param serviceName 服务名称
     */
    public static void remove(String serviceName){
        map.remove(serviceName);
    }
}
