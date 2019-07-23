package com.tianlun.ad.service.impl;

import com.tianlun.ad.constant.CommonStatus;
import com.tianlun.ad.constant.Constants;
import com.tianlun.ad.dao.AdPlanRepository;
import com.tianlun.ad.dao.AdUnitRepository;
import com.tianlun.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.tianlun.ad.dao.unit_condition.AdUnitItRepository;
import com.tianlun.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.tianlun.ad.entity.AdPlan;
import com.tianlun.ad.entity.AdUnit;
import com.tianlun.ad.entity.unit_condition.AdUnitDistrict;
import com.tianlun.ad.entity.unit_condition.AdUnitIt;
import com.tianlun.ad.entity.unit_condition.AdUnitKeyword;
import com.tianlun.ad.exception.AdException;
import com.tianlun.ad.service.IAdUnitService;
import com.tianlun.ad.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdUnitServiceImpl implements IAdUnitService {

    private final AdPlanRepository adPlanRepository;
    private final AdUnitRepository adUnitRepository;
    private final AdUnitKeywordRepository adUnitKeywordRepository;
    private final AdUnitItRepository adUnitItRepository;
    private final AdUnitDistrictRepository adUnitDistrictRepository;

    public AdUnitServiceImpl(AdPlanRepository adPlanRepository,
                             AdUnitRepository adUnitRepository,
                             AdUnitKeywordRepository adUnitKeywordRepository,
                             AdUnitItRepository adUnitItRepository,
                             AdUnitDistrictRepository adUnitDistrictRepository) {
        this.adPlanRepository = adPlanRepository;
        this.adUnitRepository = adUnitRepository;
        this.adUnitKeywordRepository = adUnitKeywordRepository;
        this.adUnitItRepository = adUnitItRepository;
        this.adUnitDistrictRepository = adUnitDistrictRepository;
    }

    @Override
    @Transactional
    public AdUnitResponse createUnit(AdUnitRequest request) throws AdException {
        if (!request.createValidate()) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        Optional<AdPlan> adPlan = adPlanRepository.findById(request.getPlanId());
        if (!adPlan.isPresent()) {
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }

        AdUnit oldAdUnit = adUnitRepository.findByPlanIdAndUnitName(request.getPlanId(), request.getUnitName());

        if (oldAdUnit != null) {
            throw new AdException(Constants.ErrorMsg.SAME_NAME_UNIT_ERROR);
        }

        AdUnit newAdUnit = adUnitRepository.save(
                new AdUnit(request.getPlanId(),
                request.getUnitName(),
                request.getPositionType(),
                request.getBudget()));

        return new AdUnitResponse(newAdUnit.getId(), newAdUnit.getUnitName());
    }

    @Override
    public AdUnit getAdUnitByUnitName(AdUnitGetRequest request) throws AdException {

        if (!request.validate()) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        return adUnitRepository.findByPlanIdAndUnitName(request.getPlanId(), request.getUnitName());
    }

    @Override
    @Transactional
    public AdUnitResponse updateAdUnit(AdUnitRequest request) throws AdException {
        if (!request.updateValidate()) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        AdUnit unit = adUnitRepository.findByPlanIdAndUnitName(request.getPlanId(), request.getUnitName());
        if (unit == null) {
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }

        if (request.getUnitName() != null) {
            unit.setUnitName(request.getUnitName());
        }
        if (request.getPositionType() != null) {
            unit.setPositionType(request.getPositionType());
        }
        if (request.getBudget() != null) {
            unit.setBudget(request.getBudget());
        }
        unit.setUpdateTime(new Date());
        unit = adUnitRepository.save(unit);
        return new AdUnitResponse(unit.getId(), unit.getUnitName());
    }

    @Override
    @Transactional
    public void deleteAdUnit(AdUnitRequest request) throws AdException {
        if (!request.deleteValidate()) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        AdUnit unit = adUnitRepository.findByPlanIdAndUnitName(request.getPlanId(), request.getUnitName());

        if (unit == null) {
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }

        unit.setUnitStatus(CommonStatus.INVALID.getStatus());
        unit.setUpdateTime(new Date());
        adUnitRepository.save(unit);
    }

    @Override
    public AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdException {
        List<Long> unitIds = request.getUnitKeywords().stream()
                .map(AdUnitKeywordRequest.UnitKeyword::getUnitId)
                .collect(Collectors.toList());

        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<Long> ids = Collections.emptyList();

        List<AdUnitKeyword> unitKeywords = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitKeywords())) {
            request.getUnitKeywords().forEach(i -> unitKeywords.add(
                    new AdUnitKeyword(i.getUnitId(), i.getKeyword())
            ));
        }
        ids = adUnitKeywordRepository.saveAll(unitKeywords).stream().
                map(AdUnitKeyword::getId).
                collect(Collectors.toList());

        return new AdUnitKeywordResponse(ids);

    }

    @Override
    public AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException {
        List<Long> unitIds = request.getUnitIts().stream()
                .map(AdUnitItRequest.UnitIt::getUnitId)
                .collect(Collectors.toList());

        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<Long> ids = Collections.emptyList();

        List<AdUnitIt> unitIts = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitIts())) {
            request.getUnitIts().forEach(i -> unitIts.add(
                    new AdUnitIt(i.getUnitId(), i.getItTag())
            ));
        }
        ids = adUnitItRepository.saveAll(unitIts).stream().
                map(AdUnitIt::getId).
                collect(Collectors.toList());

        return new AdUnitItResponse(ids);
    }

    @Override
    public AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException {
        List<Long> unitIds = request.getUnitDistricts().stream()
                .map(AdUnitDistrictRequest.UnitDistrict::getUnitId)
                .collect(Collectors.toList());

        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<AdUnitDistrict> unitDistricts = new ArrayList<>();
        request.getUnitDistricts().forEach(d -> unitDistricts.add(
                new AdUnitDistrict(d.getUnitId(), d.getProvince(), d.getCity())
        ));

        List ids = adUnitDistrictRepository.saveAll(unitDistricts)
                .stream().map(AdUnitDistrict::getId)
                .collect(Collectors.toList());

        return new AdUnitDistrictResponse(ids);
    }

    private boolean isRelatedUnitExist(List<Long> unitIds) {
        if (CollectionUtils.isEmpty(unitIds)) {
            return false;
        }

        return adUnitRepository.findAllById(unitIds).size() == new HashSet<>(unitIds).size();
    }
}
