package com.rom.example.provider;

import java.util.ArrayList;
import java.util.List;

import com.rom.example.common.service.UserService;

import com.rom.romrpc.bootstrap.ProviderBootstrap;
import com.rom.romrpc.model.ServiceRegisterInfo;


/**
 * 提供者示例
 * @author 
 */
public class ProviderExample {
    public static void main(String[] args) {

         // 要注册的服务
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo<UserService> serviceRegisterInfo = new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        // 服务提供者初始化
        ProviderBootstrap.init(serviceRegisterInfoList);

    }
}
