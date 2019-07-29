package com.tianlun.ad.handler;

import com.alibaba.fastjson.JSON;
import com.tianlun.ad.dump.table.*;
import com.tianlun.ad.index.DataTable;
import com.tianlun.ad.index.IndexAware;
import com.tianlun.ad.index.adplan.AdPlanIndex;
import com.tianlun.ad.index.adplan.AdPlanObject;
import com.tianlun.ad.index.adunit.AdUnitIndex;
import com.tianlun.ad.index.adunit.AdUnitObject;
import com.tianlun.ad.index.creative.CreativeIndex;
import com.tianlun.ad.index.creative.CreativeObject;
import com.tianlun.ad.index.creativeUnit.CreativeUnitIndex;
import com.tianlun.ad.index.creativeUnit.CreativeUnitObject;
import com.tianlun.ad.index.district.UnitDistrictIndex;
import com.tianlun.ad.index.interest.UnitItIndex;
import com.tianlun.ad.index.keyword.UnitKeywordIndex;
import com.tianlun.ad.mysql.constant.OpType;
import com.tianlun.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 1. 索引之间有层级的划分，也就是依赖关系的划分
 * 2. 加载全量索引其实是增量索引"添加"的特殊实现
 */
@Slf4j
public class AdLevelDataHandler {

    public static void handleLevel2(AdPlanTable planTable, OpType type) {

        AdPlanObject planObject = new AdPlanObject(
                planTable.getPlanId(),
                planTable.getUserId(),
                planTable.getPlanStatus(),
                planTable.getStartDate(),
                planTable.getEndDate()
        );

        handleBinlogEvent(
                DataTable.of(AdPlanIndex.class),
                planObject.getPlanId(),
                planObject,
                type
        );
    }

    public static void handleLevel2(CreativeTable creativeTable, OpType type) {
        CreativeObject creativeObject = new CreativeObject(
                creativeTable.getAdId(),
                creativeTable.getName(),
                creativeTable.getType(),
                creativeTable.getMaterialType(),
                creativeTable.getHeight(),
                creativeTable.getWidth(),
                creativeTable.getAuditStatus(),
                creativeTable.getAdUrl()
        );

        handleBinlogEvent(
                DataTable.of(CreativeIndex.class),
                creativeObject.getAdId(),
                creativeObject,
                type
        );
    }

    public static void handleLevel3(AdUnitTable adUnitTable, OpType type) {

        // 依赖关系的限制：查找所属adPlan，如果不存在说明这个unit不应该操作
        AdPlanObject adPlanObject = DataTable.of(AdPlanIndex.class).get(adUnitTable.getPlanId());
        if (adPlanObject == null) {
            log.error("handleLevel3 found AdPlanObject error: {}", adUnitTable.getPlanId());
            return;
        }

        AdUnitObject adUnitObject = new AdUnitObject(
                adUnitTable.getUnitId(),
                adUnitTable.getUnitStatus(),
                adUnitTable.getPositionType(),
                adUnitTable.getPlanId(),
                adPlanObject
        );

        handleBinlogEvent(
                DataTable.of(AdUnitIndex.class),
                adUnitTable.getUnitId(),
                adUnitObject,
                type
        );
    }

    public static void handleLevel3(AdCreativeUnitTable creativeUnitTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("CreativeUnitIndex does not support updating");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(creativeUnitTable.getUnitId());
        CreativeObject creativeObject = DataTable.of(CreativeIndex.class).get(creativeUnitTable.getAdId());
        if (unitObject == null || creativeObject == null) {
            log.error("AdCreativeUnitObject index error: {}", JSON.toJSONString(creativeObject));
            return;
        }

        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(
                creativeUnitTable.getAdId(),
                creativeUnitTable.getUnitId()
        );
        handleBinlogEvent(
                DataTable.of(CreativeUnitIndex.class),
                CommonUtils.stringConcat(creativeUnitObject.getAdId().toString(), creativeUnitObject.getUnitId().toString()),
                creativeUnitObject,
                type
        );
    }

    public static void handleLevel4(AdUnitDistrictTable adUnitDistrictTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("DistrictIndex does not support updating");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(adUnitDistrictTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitDistrictTable index error: {}", adUnitDistrictTable.getUnitId());
            return;
        }

        String key = CommonUtils.stringConcat(adUnitDistrictTable.getProvince(), adUnitDistrictTable.getCity());
        Set<Long> value = new HashSet<>(Collections.singleton(adUnitDistrictTable.getUnitId()));
        handleBinlogEvent(
                DataTable.of(UnitDistrictIndex.class),
                key,
                value,
                type
        );
    }

    public static void handleLevel4(AdUnitItTable adUnitItTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("ItIndex does not support updating");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(adUnitItTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitItTable index error: {}", adUnitItTable.getUnitId());
            return;
        }
        Set<Long> value = new HashSet<>(Collections.singleton(adUnitItTable.getUnitId()));
        handleBinlogEvent(
                DataTable.of(UnitItIndex.class),
                adUnitItTable.getItTag(),
                value,
                type
        );
    }

    public static void handleLevel4(AdUnitKeywordTable adUnitKeywordTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("KeywordIndex does not support updating");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(adUnitKeywordTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitKeywordTable index error: {}", adUnitKeywordTable.getUnitId());
            return;
        }
        Set<Long> value = new HashSet<>(Collections.singleton(adUnitKeywordTable.getUnitId()));
        handleBinlogEvent(
                DataTable.of(UnitKeywordIndex.class),
                adUnitKeywordTable.getKeyword(),
                value,
                type
        );
    }

    private static <K, V> void handleBinlogEvent(IndexAware<K, V> index, K key, V value, OpType type) {
        switch (type) {
            case ADD:
                index.add(key, value);
                break;
            case UPDATE:
                index.update(key, value);
            case DELETE:
                index.delete(key, value);
                break;
            default:
                break;
        }
    }
}
