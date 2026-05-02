package com.rom.romrpc.fault.retry;

import java.util.concurrent.Callable;

import com.rom.romrpc.model.RpcResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 不重试 - 重试策略
 *
 */
@Slf4j
public class NoRetryStrategy implements RetryStrategy {

    /**
     * 重试
     *
     * @param callable
     * @return
     * @throws Exception
     */
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }

}

