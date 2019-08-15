package com.tianlun.ad.consumer;

import com.alibaba.fastjson.JSON;
import com.tianlun.ad.dto.MySqlRowData;
import com.tianlun.ad.sender.ISender;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class BinlogConsumer {

    private final ISender sender;

    public BinlogConsumer(ISender sender) {
        this.sender = sender;
    }

    @KafkaListener(topics = {"ad-search-mysql-data"}, groupId = "ad-search")
    public void processMysqlRowData(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            MySqlRowData rowData = JSON.parseObject(
                    message.toString(),
                    MySqlRowData.class
            );

            log.info("kafka process mysql row data: {}", JSON.toJSONString(rowData));
            sender.sender(rowData);
        }
    }
}
