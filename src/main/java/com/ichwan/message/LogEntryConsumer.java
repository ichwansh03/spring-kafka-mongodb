package com.ichwan.message;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class LogEntryConsumer {

    @KafkaListener(topics = "${kafka.topic.log-entry}", groupId = "log-entry-group", containerFactory = "logEntryKafkaListenerContainerFactory" )
    public void consumeLogEntry(String message) {
        System.out.println("Consumed log entry: " + message);
    }

    @KafkaListener(topics = "${kafka.topic.mongo-data}", groupId = "mongo-data-group", containerFactory = "mongoDataKafkaListenerContainerFactory" )
    public void consumeMongoData(LogEntry logEntry) {
        System.out.println("Consumed log entry for MongoDB: " + logEntry);
    }
}
