package com.tianlun.ad.service;

import com.tianlun.ad.entity.AdUnit;
import com.tianlun.ad.exception.AdException;
import com.tianlun.ad.vo.*;

public interface IAdUnitService {

    AdUnitResponse createUnit(AdUnitRequest request) throws AdException;

    AdUnit getAdUnitByUnitName(AdUnitGetRequest request) throws AdException;

    AdUnitResponse updateAdUnit(AdUnitRequest request) throws AdException;

    void deleteAdUnit(AdUnitRequest request) throws AdException;

    AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdException;

    AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException;

    AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException;

    // TODO: Add delete and get for unit conditions

    CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException;

    // TODO: Add get update and delete service
}
