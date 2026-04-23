package com.rom.example.provider;

import com.rom.example.common.service.UserService;
import com.rom.romrpc.registry.LocalRegistry;
import com.rom.romrpc.server.HttpServer;
import com.rom.romrpc.server.VertxHttpServer;

/**
 * 易用提供者示例
 * */
public class EasyProviderExample {
    public static void main(String[] args) {
        //注册服务
        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);

        //启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
        
    }
}
