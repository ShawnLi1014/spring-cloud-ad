package com.tianlun.ad.service;

import com.tianlun.ad.exception.AdException;
import com.tianlun.ad.vo.CreativeRequest;
import com.tianlun.ad.vo.CreativeResponse;

public interface ICreativeService {

    CreativeResponse createCreative(CreativeRequest request) throws AdException;
    // TODO: Add update read and delete service
}
