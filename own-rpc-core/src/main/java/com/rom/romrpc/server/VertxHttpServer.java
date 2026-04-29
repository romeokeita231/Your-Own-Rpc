package com.rom.romrpc.server;

import io.vertx.core.Vertx;
/**
 * Vert.x HTTP服务器实现
 * */
public class VertxHttpServer implements HttpServer {

    /**
     * 启动HTTP服务器
     * @param port 监听端口
     */
    public void doStart(int port) {
        //创建Vert.x实例
        Vertx vertx = Vertx.vertx();
        //创建HTTP服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        //监听端口并处理请求
        server.requestHandler(new HttpServerHandle());

        //启动服务器
        server.listen(port,result -> {
            if(result.succeeded()){
                System.out.println("服务器启动成功，监听端口：" + port);
            }else{
                System.out.println("服务器启动失败：" + result.cause());
            }
        });
    }
}
