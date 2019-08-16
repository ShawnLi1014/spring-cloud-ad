package com.tianlun.ad.sender.kafka;

import com.alibaba.fastjson.JSON;
import com.tianlun.ad.dto.MySqlRowData;
import com.tianlun.ad.sender.IKafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaSender implements IKafkaSender {

    @Value("${adconf.kafka.topic}")
    private String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sender(MySqlRowData rowData) {
        log.info("Binlog kafka service send MySqlRowData");
        kafkaTemplate.send(topic, JSON.toJSONString(rowData));
    }

}
