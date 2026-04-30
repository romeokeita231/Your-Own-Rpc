package com.rom.example.provider;

import com.rom.example.common.service.UserService;
import com.rom.romrpc.RpcApplication;
import com.rom.romrpc.config.RegistryConfig;
import com.rom.romrpc.config.RpcConfig;
import com.rom.romrpc.model.ServiceMetaInfo;
import com.rom.romrpc.registry.LocalRegistry;
import com.rom.romrpc.registry.Registry;
import com.rom.romrpc.registry.RegistryFactory;
import com.rom.romrpc.server.HttpServer;
import com.rom.romrpc.server.VertxHttpServer;

/**
 * 提供者示例
 * @author 
 */
public class ProviderExample {
    public static void main(String[] args) {
        // RPC 框架初始化
        RpcApplication.init();
        
        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

         // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
