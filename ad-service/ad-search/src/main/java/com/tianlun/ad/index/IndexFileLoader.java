package com.tianlun.ad.index;

import com.alibaba.fastjson.JSON;
import com.tianlun.ad.dump.DConstant;
import com.tianlun.ad.dump.table.*;
import com.tianlun.ad.handler.AdLevelDataHandler;
import com.tianlun.ad.mysql.constant.OpType;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
@DependsOn("dataTable")
public class IndexFileLoader {

    @PostConstruct
    public void init() {
        List<String> adPlanStrings = loadDumpData(
                String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_PLAN)
        );
        adPlanStrings.forEach(p -> AdLevelDataHandler.handleLevel2(
                JSON.parseObject(p, AdPlanTable.class),
                OpType.ADD
        ));

        List<String> adCreativeStrings = loadDumpData(
                String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_CREATIVE)
        );
        adCreativeStrings.forEach(c -> AdLevelDataHandler.handleLevel2(
                JSON.parseObject(c, CreativeTable.class),
                OpType.ADD
        ));

        List<String> adUnitStrings = loadDumpData(
                String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT)
        );
        adUnitStrings.forEach(u -> AdLevelDataHandler.handleLevel3(
                JSON.parseObject(u, AdUnitTable.class),
                OpType.ADD
        ));

        List<String> adCreativeUnitStrings = loadDumpData(
                String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_CREATIVE_UNIT)
        );
        adCreativeUnitStrings.forEach(cu -> AdLevelDataHandler.handleLevel3(
                JSON.parseObject(cu, AdCreativeUnitTable.class),
                OpType.ADD
        ));

        List<String> adUnitDistrictStrings = loadDumpData(
                String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_DISTRICT)
        );
        adUnitDistrictStrings.forEach(ud -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(ud, AdUnitDistrictTable.class),
                OpType.ADD
        ));

        List<String> adUnitItStrings = loadDumpData(
                String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_IT)
        );
        adUnitItStrings.forEach(ui -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(ui, AdUnitItTable.class),
                OpType.ADD
        ));

        List<String> adUnitKeywordStrings = loadDumpData(
                String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_KEYWORD)
        );
        adUnitKeywordStrings.forEach(uk -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(uk, AdUnitKeywordTable.class),
                OpType.ADD
        ));
    }

    private List<String> loadDumpData(String fileName) {
        try (BufferedReader br = Files.newBufferedReader(
                Paths.get(fileName)
        )){

            return br.lines().collect(Collectors.toList());

        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
