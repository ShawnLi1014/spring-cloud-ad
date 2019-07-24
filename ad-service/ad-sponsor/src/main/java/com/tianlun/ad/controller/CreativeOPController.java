package com.tianlun.ad.controller;

import com.alibaba.fastjson.JSON;
import com.tianlun.ad.exception.AdException;
import com.tianlun.ad.service.ICreativeService;
import com.tianlun.ad.vo.CreativeRequest;
import com.tianlun.ad.vo.CreativeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CreativeOPController {

    private final ICreativeService creativeService;

    public CreativeOPController(ICreativeService creativeService) {
        this.creativeService = creativeService;
    }

    @PostMapping("/create/creative")
    public CreativeResponse createCreative (@RequestBody CreativeRequest request) throws AdException {
        log.info("ad-sponsor: createCreative -> {}", JSON.toJSONString(request));
        return creativeService.createCreative(request);
    }
}
