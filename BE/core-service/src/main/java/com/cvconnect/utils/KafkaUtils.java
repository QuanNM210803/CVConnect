package com.cvconnect.utils;

import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaUtils {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendWithJson(String topic, Object message) {
        String json = ObjectMapperUtils.convertToJson(message);
        kafkaTemplate.send(topic, json);
    }
}
