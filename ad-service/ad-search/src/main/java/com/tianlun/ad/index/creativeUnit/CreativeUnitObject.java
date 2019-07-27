package com.tianlun.ad.index.creativeUnit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreativeUnitObject {

    private Long adId;
    private Long unitId;

    // adId-unitId as key to locate creativeUnitObject
}
