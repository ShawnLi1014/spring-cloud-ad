package com.tianlun.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitGetRequest {

    private Long planId;
    private String unitName;

    public boolean validate() {
        return planId != null && planId > 0 && !StringUtils.isEmpty(unitName);
    }
}
