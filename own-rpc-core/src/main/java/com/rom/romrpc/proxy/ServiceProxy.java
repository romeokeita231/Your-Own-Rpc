package com.rom.romrpc.proxy;

import cn.hutool.core.collection.CollUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rom.romrpc.RpcApplication;
import com.rom.romrpc.config.RpcConfig;
import com.rom.romrpc.constant.RpcConstant;
import com.rom.romrpc.fault.retry.RetryStrategy;
import com.rom.romrpc.fault.retry.RetryStrategyFactory;
import com.rom.romrpc.fault.tolerant.TolerantStrategy;
import com.rom.romrpc.fault.tolerant.TolerantStrategyFactory;
import com.rom.romrpc.loadbalancer.LoadBalancer;
import com.rom.romrpc.loadbalancer.LoadBalancerFactory;
import com.rom.romrpc.model.RpcRequest;
import com.rom.romrpc.model.RpcResponse;
import com.rom.romrpc.model.ServiceMetaInfo;
import com.rom.romrpc.registry.Registry;
import com.rom.romrpc.registry.RegistryFactory;
import com.rom.romrpc.server.tcp.VertxTcpClient;

/**
 * 服务代理（JDK 动态代理）
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 过滤 Object 类的方法（toString、hashCode、equals 等）
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }


        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            
            

            // 从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }

            // 负载均衡
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            // 将调用方法名（请求路径）作为负载均衡参数
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("methodName", rpcRequest.getMethodName());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
            
            // rpc 请求
            // 使用重试机制
            RpcResponse rpcResponse;
            try {
                RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
                rpcResponse = retryStrategy.doRetry(() ->
                        VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo)
                );
            } catch (Exception e) {
                // 容错机制
                TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
                rpcResponse = tolerantStrategy.doTolerant(null, e);
            }
            return rpcResponse.getData();


        } catch (Exception e) {
            throw new RuntimeException("调用失败");
        }        
    }
}

