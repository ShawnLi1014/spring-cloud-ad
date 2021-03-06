package com.tianlun.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.event.EventType;
import com.tianlun.ad.constant.Constant;
import com.tianlun.ad.constant.OpType;
import com.tianlun.ad.dto.BinlogRowData;
import com.tianlun.ad.dto.MySqlRowData;
import com.tianlun.ad.dto.TableTemplate;
import com.tianlun.ad.sender.IKafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class IncrementListener implements Ilistener {

    @Resource
    private IKafkaSender sender;

    private final AggregationListener aggregationListener;

    public IncrementListener(AggregationListener aggregationListener) {
        this.aggregationListener = aggregationListener;
    }

    @Override
    @PostConstruct
    public void register() {

        log.info("Increment listener register db and table info");
        Constant.table2DB.forEach((k, v) ->
                aggregationListener.register(v, k, this));

    }

    @Override
    public void onEvent(BinlogRowData eventData) {

        TableTemplate table = eventData.getTable();
        EventType eventType = eventData.getEventType();

        // 包装成最后需要投递的数据 即 MySqlRowData
        MySqlRowData rowData = new MySqlRowData();
        rowData.setTableName(table.getTableName());
        rowData.setLevel(eventData.getTable().getLevel());
        OpType opType = OpType.to(eventType);
        rowData.setOpType(opType);

        // 取出模板中该操作对应的字段列表
        List<String> fieldList = table.getOpTypeFieldSetMap().get(opType);
        if (null == fieldList) {
            log.warn("{} not support for {}", opType, table.getTableName());
        }

        for (Map<String, String> afterMap : eventData.getAfter()) {
            Map<String, String> _afterMap = new HashMap<>();
            for (Map.Entry<String, String> entry : afterMap.entrySet()) {
                String colName = entry.getKey();
                String colValue = entry.getValue();

                _afterMap.put(colName, colValue);
            }
            rowData.getFieldValueMap().add(_afterMap);
        }

        sender.sender(rowData);

    }
}
