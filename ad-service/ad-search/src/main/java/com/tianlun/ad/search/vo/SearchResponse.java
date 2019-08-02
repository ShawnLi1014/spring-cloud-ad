package com.tianlun.ad.search.vo;

import com.tianlun.ad.index.creative.CreativeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {

    public Map<String, List<Creative>> adSlot2Ads = new HashMap<>();


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Creative {

        private Long adId;
        private String adUrl;
        private Integer width;
        private Integer height;
        private Integer type;
        private Integer materialType;

        // 展示监测url
        private List<String> showMonitorUrl = Arrays.asList("www.tianlun.com", "www.tianlun.com");
        // 点击监测url
        private List<String> clickMonitorUrl = Arrays.asList("www.tianlun.com", "www.tianlun.com");
    }

    // 检索服务实现的是对索引的检索，返回的是CreativeObject，需转化成我们定义的Creative
    public static Creative convert(CreativeObject object) {

        Creative creative = new Creative();
        creative.setAdId(object.getAdId());
        creative.setAdUrl(object.getAdUrl());
        creative.setWidth(object.getWidth());
        creative.setHeight(object.getHeight());
        creative.setType(object.getType());
        creative.setMaterialType(object.getMaterialType());

        return creative;
    }
}
