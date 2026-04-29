package com.rom.example.consumer;

import com.rom.romrpc.config.RpcConfig;
import com.rom.romrpc.utils.ConfigUtils;

/**
 * 消费者示例
 * @author 
 */
public class ConsumerExample {
    
    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
    }
}
