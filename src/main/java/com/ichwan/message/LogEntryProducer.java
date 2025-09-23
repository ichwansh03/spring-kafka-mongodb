package com.ichwan.message;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LogEntryProducer {

    private final RoutingKafkaTemplate kafkaTemplate;

    @Value("${kafka.topic.log-entry}")
    private String logEntriesTopic;

    @Value("${kafka.topic.mongo-data}")
    private String mongoDataTopic;

    public LogEntryProducer(RoutingKafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(LogEntry logEntry) {
        kafkaTemplate.send(logEntriesTopic, logEntry.getMessage(), logEntry.toString());
        kafkaTemplate.send(mongoDataTopic, logEntry.getTimestamp().toString(), logEntry);
    }
}
