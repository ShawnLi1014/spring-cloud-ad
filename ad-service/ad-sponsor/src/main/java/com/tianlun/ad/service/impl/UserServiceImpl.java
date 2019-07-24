package com.tianlun.ad.service.impl;

import com.tianlun.ad.constant.Constants;
import com.tianlun.ad.dao.AdUserRepository;
import com.tianlun.ad.entity.AdUser;
import com.tianlun.ad.exception.AdException;
import com.tianlun.ad.service.IUserService;
import com.tianlun.ad.utils.CommonUtils;
import com.tianlun.ad.vo.CreateUserRequest;
import com.tianlun.ad.vo.CreateUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final AdUserRepository adUserRepository;

    public UserServiceImpl(AdUserRepository adUserRepository) {
        this.adUserRepository = adUserRepository;
    }

    @Override
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) throws AdException {
        if(!request.validate()) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        AdUser oldUser = adUserRepository.findByUsername(request.getUsername());
        if(oldUser != null) {
            throw new AdException(Constants.ErrorMsg.SAME_NAME_ERROR);
        }

        AdUser newUser = adUserRepository.save(new AdUser(
                request.getUsername(),
                CommonUtils.md5(request.getUsername())));
        CreateUserResponse cur = new CreateUserResponse(newUser.getId(), newUser.getUsername(), newUser.getToken(),
                newUser.getCreateTime(), newUser.getUpdateTime());
        System.out.println(cur.getUsername());
        return cur;
    }
}
