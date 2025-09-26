package com.ichwan.message;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Configuration
@EnableKafka
public class LogEntryConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrap;

    @Value("${kafka.topic.log-entry}")
    private String logEntriesTopic;

    @Value("${spring.kafka.consumer.group-id.log-entry}")
    private String logEntryGroupId;

    @Value("${kafka.topic.mongo-data}")
    private String mongoDataTopic;

    @Value("${spring.kafka.consumer.group-id.mongo-data}")
    private String mongoDataGroupId;

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

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LogEntry> mongoDataKafkaListenerContainerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, mongoDataGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.ichwan.message.LogEntry");
        DefaultKafkaConsumerFactory<String, LogEntry> consumerFactory = new DefaultKafkaConsumerFactory<>(props);
        ConcurrentKafkaListenerContainerFactory<String, LogEntry> container = new ConcurrentKafkaListenerContainerFactory<>();
        container.setConsumerFactory(consumerFactory);
        return container;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> logEntryKafkaListenerContainerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, logEntryGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(props);
        ConcurrentKafkaListenerContainerFactory<String, String> container = new ConcurrentKafkaListenerContainerFactory<>();
        container.setConsumerFactory(consumerFactory);
        return container;
    }
}
