package com.rom.example.provider;

import com.rom.example.common.service.UserService;
import com.rom.romrpc.RpcApplication;
import com.rom.romrpc.registry.LocalRegistry;
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
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
