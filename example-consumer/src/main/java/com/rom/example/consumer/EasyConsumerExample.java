package com.rom.example.consumer;

import com.rom.example.common.model.User;
import com.rom.example.common.service.UserService;
import com.rom.romrpc.proxy.ServiceProxyFactory;

/**
 * 易用消费者示例
 * */
public class EasyConsumerExample {
    public static void main(String[] args) {
        //动态代理获取UserService的实现类对象
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("rom");
        //调用
        User newUser = userService.getUser(user);
        if(newUser != null) {
            System.out.println("用户名: " + newUser.getName());
        }else{
            System.out.println("用户名不存在");
        }
    }
}
