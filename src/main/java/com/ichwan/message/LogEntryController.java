package com.ichwan.message;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
public class LogEntryController {

    private final LogEntryProducer producer;

    public LogEntryController(LogEntryProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public String createLog(@RequestBody LogEntry logEntry) {
        producer.send(logEntry);
        return "Log sent to Kafka successfully!";
    }

}
