package com.ichwan.message;

import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class LogListenerManager {

    private final KafkaListenerEndpointRegistry registry;

    public LogListenerManager(KafkaListenerEndpointRegistry registry) {
        this.registry = registry;
    }

    public void startListeners(String listenerId) {
        MessageListenerContainer listenerContainer = registry.getListenerContainer(listenerId);
        if (listenerContainer != null && !listenerContainer.isRunning()) {
            listenerContainer.start();
            System.out.println("Started listener with ID: " + listenerId);
        } else {
            System.out.println("Listener with ID: " + listenerId + " is already running or does not exist.");
        }
    }

    public void stopListeners(String listenerId) {
        MessageListenerContainer listenerContainer = registry.getListenerContainer(listenerId);
        if (listenerContainer != null && listenerContainer.isRunning()) {
            listenerContainer.stop();
            System.out.println("Stopped listener with ID: " + listenerId);
        } else {
            System.out.println("Listener with ID: " + listenerId + " is already stopped or does not exist.");
        }
    }
}
