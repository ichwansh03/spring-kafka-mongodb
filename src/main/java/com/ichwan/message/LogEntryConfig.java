package com.ichwan.message;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Configuration
public class LogEntryConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrap;

    @Value("${kafka.topic.log-entry}")
    private String logEntriesTopic;

    @Value("${kafka.topic.mongo-data}")
    private String mongoDataTopic;

    @Bean
    public KafkaAdmin.NewTopics logTopic() {
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name(logEntriesTopic)
                        .partitions(3)
                        .replicas(1)
                        .config(TopicConfig.COMPRESSION_TYPE_CONFIG,"gzip")
                        .build(),
                TopicBuilder.name(mongoDataTopic)
                        .partitions(3)
                        .replicas(1)
                        .compact()
                        .build()
        );

    }

    @Bean
    public RoutingKafkaTemplate routingKafkaTemplate() {
        Map<Pattern, ProducerFactory<Object, Object>> factoryMap = new HashMap<>();

        //json producer
        Map<String, Object> jsonProps = new HashMap<>();
        jsonProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        jsonProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        jsonProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        factoryMap.put(Pattern.compile(mongoDataTopic), new DefaultKafkaProducerFactory<>(jsonProps));

        //string producer
        Map<String, Object> stringProps = new HashMap<>();
        stringProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        stringProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        stringProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        factoryMap.put(Pattern.compile("log-.*"), new DefaultKafkaProducerFactory<>(stringProps));

        return new RoutingKafkaTemplate(factoryMap);
    }

}
