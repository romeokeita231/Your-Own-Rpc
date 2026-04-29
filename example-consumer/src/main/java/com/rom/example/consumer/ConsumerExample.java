package com.rom.example.consumer;

import com.rom.example.common.model.User;
import com.rom.example.common.service.UserService;
import com.rom.romrpc.proxy.ServiceProxyFactory;

/**
 * 消费者示例
 * @author 
 */
public class ConsumerExample {
    
    public static void main(String[] args) {
        // 获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("rom");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
        long number = userService.getNumber();
        System.out.println(number);
    }

}
