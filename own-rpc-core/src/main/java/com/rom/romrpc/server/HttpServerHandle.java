package com.rom.romrpc.server;

import java.lang.reflect.Method;

import com.rom.romrpc.model.RpcRequest;
import com.rom.romrpc.model.RpcResponse;
import com.rom.romrpc.registry.LocalRegistry;
import com.rom.romrpc.serializer.JdkSerializer;
import com.rom.romrpc.serializer.Serializer;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;


/**
 * HTTP 请求处理
 */
public class HttpServerHandle implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest request) {
        // 指定序列化器
        final Serializer serializer = new JdkSerializer();

        //记录日志
        System.out.println("收到请求：" + request.method() + " " + request.uri());

        //异步处理请求
        request.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            //如果请求为null，直接返回
            if (rpcRequest == null) {
                rpcResponse.setMessage("rpc请求为空");
                doResponse(request, rpcResponse, serializer);
                return;
            }
            System.out.println("请求服务: " + rpcRequest.getServiceName());
            try {
                //获取要调用的服务实现类，通过反射调用
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                if (implClass == null) {
                    throw new RuntimeException("服务不存在: " + rpcRequest.getServiceName() + "，请检查 Provider 是否注册该服务");
                }
                Method method = implClass.getMethod(rpcRequest.getMethodName(),rpcRequest.getParameterTypes());
                Object result = method.invoke(implClass.getDeclaredConstructor().newInstance(), rpcRequest.getArgs());
                //封装返回结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            //响应
            doResponse(request, rpcResponse, serializer);
        });
    }

    /**
     * 响应
     * @param request
     * @param rpcResponse
     * @param serializer
     */
    void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = 
                request.response()
                       .putHeader("Content-Type","application/json");
        try {
                byte[] serialized = serializer.serialize(rpcResponse);
                httpServerResponse.end(Buffer.buffer(serialized));
        } catch (Exception e) {
                e.printStackTrace();
                httpServerResponse.end(Buffer.buffer());
        }
    }
}