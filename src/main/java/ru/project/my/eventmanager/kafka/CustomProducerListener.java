package ru.project.my.eventmanager.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;
import ru.project.my.eventmanager.kafka.model.EventChangeMessage;

@Component
public class CustomProducerListener implements ProducerListener<Long, EventChangeMessage> {
    static final Logger log = LoggerFactory.getLogger(CustomProducerListener.class);

    @Override
    public void onSuccess(ProducerRecord<Long, EventChangeMessage> producerRecord, RecordMetadata recordMetadata) {
        log.info("В топик {} отправлено сообщение {}", recordMetadata, producerRecord.value());
    }

    @Override
    public void onError(ProducerRecord<Long, EventChangeMessage> producerRecord, RecordMetadata recordMetadata, Exception exception) {
        log.error("Ошибка при отправке в топик {}", recordMetadata, exception);
    }
}
