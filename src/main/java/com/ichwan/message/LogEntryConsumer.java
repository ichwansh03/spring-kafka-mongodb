package com.ichwan.message;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class LogEntryConsumer {

    private final LogRepository logRepository;

    public LogEntryConsumer(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @KafkaListener(
            id = "log-listener",
            topics = "${kafka.topic.log-entry}",
            groupId = "${spring.kafka.consumer.group-id.log-entry}",
            containerFactory = "logEntryKafkaListenerContainerFactory",
            autoStartup = "false"
    )
    public void consumeLogEntry(String message) {
        System.out.println("Consumed log entry: " + message);
    }

    @KafkaListener(
            topics = "${kafka.topic.mongo-data}",
            groupId = "${spring.kafka.consumer.group-id.mongo-data}",
            containerFactory = "mongoDataKafkaListenerContainerFactory",
            autoStartup = "false"
    )
    public void consumeMongoData(LogEntry logEntry) {
        System.out.println("Consumed log entry for MongoDB: " + logEntry);
        logRepository.save(logEntry);
    }
}
