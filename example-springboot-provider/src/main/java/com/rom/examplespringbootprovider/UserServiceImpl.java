package com.rom.examplespringbootprovider;

import org.springframework.stereotype.Service;

import com.rom.example.common.model.User;
import com.rom.example.common.service.UserService;
import com.rom.romrpc.springboot.starter.annotation.RpcService;

/**
 * 用户服务实现类
 *
 */
@Service
@RpcService
public class UserServiceImpl implements UserService {

    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }
}
