package com.tianlun.ad.service;

import com.tianlun.ad.entity.AdUnit;
import com.tianlun.ad.exception.AdException;
import com.tianlun.ad.vo.AdUnitGetRequest;
import com.tianlun.ad.vo.AdUnitRequest;
import com.tianlun.ad.vo.AdUnitResponse;

public interface IAdUnitService {

    AdUnitResponse createUnit(AdUnitRequest request) throws AdException;

    AdUnit getAdUnitByUnitName(AdUnitGetRequest request) throws AdException;

    AdUnitResponse updateAdUnit(AdUnitRequest request) throws AdException;

    void deleteAdUnit(AdUnitRequest request) throws AdException;
}
