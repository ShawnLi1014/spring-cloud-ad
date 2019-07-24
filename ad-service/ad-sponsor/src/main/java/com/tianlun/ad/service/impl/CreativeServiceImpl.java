package com.tianlun.ad.service.impl;

import com.tianlun.ad.constant.Constants;
import com.tianlun.ad.dao.AdUserRepository;
import com.tianlun.ad.dao.CreativeRepository;
import com.tianlun.ad.entity.AdUser;
import com.tianlun.ad.entity.Creative;
import com.tianlun.ad.exception.AdException;
import com.tianlun.ad.service.ICreativeService;
import com.tianlun.ad.vo.CreativeRequest;
import com.tianlun.ad.vo.CreativeResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CreativeServiceImpl implements ICreativeService {

    private final CreativeRepository creativeRepository;
    private final AdUserRepository adUserRepository;

    public CreativeServiceImpl(CreativeRepository creativeRepository,
                               AdUserRepository adUserRepository) {
        this.creativeRepository = creativeRepository;
        this.adUserRepository = adUserRepository;
    }

    @Override
    @Transactional
    public CreativeResponse createCreative(CreativeRequest request) throws AdException {

        if (!request.validate()) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        Optional<AdUser> user = adUserRepository.findById(request.getUserId());
        if (user == null) {
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }

        Creative creative = creativeRepository.save(
                request.convertToEntity()
        );
        return new CreativeResponse(creative.getId(), creative.getName());
    }
}
