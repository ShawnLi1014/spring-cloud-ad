package com.tianlun.ad.service.impl;

import com.tianlun.ad.constant.CommonStatus;
import com.tianlun.ad.constant.Constants;
import com.tianlun.ad.dao.AdPlanRepository;
import com.tianlun.ad.dao.AdUnitRepository;
import com.tianlun.ad.entity.AdPlan;
import com.tianlun.ad.entity.AdUnit;
import com.tianlun.ad.exception.AdException;
import com.tianlun.ad.service.IAdUnitService;
import com.tianlun.ad.vo.AdUnitGetRequest;
import com.tianlun.ad.vo.AdUnitRequest;
import com.tianlun.ad.vo.AdUnitResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class AdUnitServiceImpl implements IAdUnitService {

    private final AdPlanRepository adPlanRepository;
    private final AdUnitRepository adUnitRepository;

    public AdUnitServiceImpl(AdPlanRepository adPlanRepository, AdUnitRepository adUnitRepository) {
        this.adPlanRepository = adPlanRepository;
        this.adUnitRepository = adUnitRepository;
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
}
