package ru.project.my.eventmanager.kafka;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;
import ru.project.my.eventmanager.kafka.model.EventChangeMessage;

import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaTemplate<Long, EventChangeMessage> kafkaTemplate(KafkaProperties kafkaProperties, ProducerListener<Long, EventChangeMessage> producerListener) {
        Map<String, Object> props = kafkaProperties.buildProducerProperties(new DefaultSslBundleRegistry());
        ProducerFactory<Long, EventChangeMessage> producerFactory = new DefaultKafkaProducerFactory<>(props);
        KafkaTemplate<Long, EventChangeMessage> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setProducerListener(producerListener);
        return kafkaTemplate;
    }
}
