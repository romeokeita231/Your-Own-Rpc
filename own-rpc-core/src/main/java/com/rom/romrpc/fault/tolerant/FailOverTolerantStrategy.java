package com.rom.romrpc.fault.tolerant;

import java.util.Map;

import com.rom.romrpc.model.RpcResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 转移到其他服务节点 - 容错策略
 *
 */
@Slf4j
public class FailOverTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // todo 可自行扩展，获取其他服务节点并调用
        return null;
    }
}

