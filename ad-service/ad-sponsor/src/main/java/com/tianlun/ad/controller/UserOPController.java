package com.tianlun.ad.controller;

import com.alibaba.fastjson.JSON;
import com.tianlun.ad.exception.AdException;
import com.tianlun.ad.service.IUserService;
import com.tianlun.ad.vo.CreateUserRequest;
import com.tianlun.ad.vo.CreateUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserOPController {

    private final IUserService userService;

    public UserOPController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create/user")
    public CreateUserResponse createUser(@RequestBody CreateUserRequest request) throws AdException {
        log.info("ad-sponsor: createUser -> {}", JSON.toJSONString(request));

        CreateUserResponse cur = userService.createUser(request);
        System.out.println(JSON.toJSONString(cur));
        return cur;
    }
}
