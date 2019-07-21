package com.tianlun.ad.service;

import com.tianlun.ad.exception.AdException;
import com.tianlun.ad.vo.CreateUserRequest;
import com.tianlun.ad.vo.CreateUserResponse;

public interface IUserService {

    /**
     * <h2>Create User</h2>
     */
    CreateUserResponse createUser(CreateUserRequest request) throws AdException;
}
