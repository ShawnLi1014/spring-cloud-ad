package com.tianlun.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitRequest {

    private Long planId;
    private String unitName;

    private Integer positionType;
    private Long budget;

    public boolean createValidate() {
        return planId != null
                && !StringUtils.isEmpty(unitName)
                && positionType != null
                && positionType > 0
                && budget != null
                && budget > 0;
    }

    public boolean updateValidate() {
        return planId != null
                && planId > 0
                && !StringUtils.isEmpty(unitName);
    }

    public boolean deleteValidate() {
        return planId != null
                && planId > 0
                && !StringUtils.isEmpty(unitName);
    }
}
