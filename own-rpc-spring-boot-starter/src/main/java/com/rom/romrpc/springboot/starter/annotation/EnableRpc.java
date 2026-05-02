package com.rom.romrpc.springboot.starter.annotation;

import java.lang.annotation.*;

import org.springframework.context.annotation.Import;

import com.rom.romrpc.springboot.starter.bootstrap.RpcConsumerBootstrap;
import com.rom.romrpc.springboot.starter.bootstrap.RpcInitBootstrap;
import com.rom.romrpc.springboot.starter.bootstrap.RpcProviderBootstrap;

/**
 * 启用 Rpc 注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableRpc {

    /**
     * 需要启动 server
     *
     * @return
     */
    boolean needServer() default true;
}
