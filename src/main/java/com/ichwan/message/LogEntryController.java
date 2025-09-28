package com.ichwan.message;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
public class LogEntryController {

    private final LogListenerManager listenerManager;
    private final LogEntryProducer producer;

    public LogEntryController(LogEntryProducer producer, LogListenerManager listenerManager) {
        this.producer = producer;
        this.listenerManager = listenerManager;
    }

    @PostMapping
    public String createLog(@RequestBody LogEntry logEntry) {
        producer.send(logEntry);
        return "Log sent to Kafka successfully!";
    }

    @GetMapping("/start")
    public String startListeners() {
        listenerManager.startListeners("log-listener");
        return "Kafka listeners started.";
    }

    @GetMapping("/stop")
    public String stopListeners() {
        listenerManager.stopListeners("log-listener");
        return "Kafka listeners stopped.";
    }
}
