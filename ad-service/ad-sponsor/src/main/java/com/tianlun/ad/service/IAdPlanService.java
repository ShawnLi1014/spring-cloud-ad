package com.tianlun.ad.service;

import com.tianlun.ad.entity.AdPlan;
import com.tianlun.ad.exception.AdException;
import com.tianlun.ad.vo.AdPlanGetRequest;
import com.tianlun.ad.vo.AdPlanRequest;
import com.tianlun.ad.vo.AdPlanResponse;

import java.util.List;

public interface IAdPlanService {

    /**
     * <h2>Create Ad Plan</h2>
     */
    AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException;

    /**
     * <h2>Get Ad Plans</h2>
     */
    List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws AdException;

    /**
     * <h2>Update Ad Plan</h2>
     */
    AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdException;

    /**
     * <h2>Delete Ad Plans</h2>
     */
    void deleteAdPlan(AdPlanRequest request) throws AdException;
}
