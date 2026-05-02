package com.rom.examplespringbootconsumer;

import org.springframework.stereotype.Service;

import com.rom.example.common.model.User;
import com.rom.example.common.service.UserService;
import com.rom.romrpc.springboot.starter.annotation.RpcReference;

/**
 * 示例服务实现类
 *
 */
@Service
public class ExampleServiceImpl {

    @RpcReference
    private UserService userService;

    public void test() {
        User user = new User();
        user.setName("yupi");
        User resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }

}
