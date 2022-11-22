package com.aws.peach.infrastructure.kafka;

import com.aws.peach.domain.support.MessageProducer;
import com.aws.peach.infrastructure.configuration.support.MessageIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.TransactionAbortedException;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;

@Slf4j
@Component
public class KafkaMessageProducerFactory {

    public <M> MessageProducer<String,M> create(final KafkaTemplate<String, M> kafkaTemplate,
                                                final String topic) {
        return (key, value) -> {
            ProducerRecord<String, M> producerRecord = new ProducerRecord<>(topic, null, null, key, value, createCustomHeaders());
            ListenableFuture<SendResult<String, M>> future = kafkaTemplate.send(producerRecord);
            future.addCallback(new KafkaSendCallback<>(){
                @Override
                public void onSuccess(SendResult<String, M> result) {
                    log.info("Sent message=[ {} ] with offset=[ {} ]", result.getProducerRecord(), result.getRecordMetadata().offset());
                }

                @Override
                public void onFailure(KafkaProducerException ex) {
                    if (ex.getCause() instanceof TransactionAbortedException) {
                        log.warn("{}, message=[ {} ]", ex.getMessage(), ex.getFailedProducerRecord());
                        return;
                    }
                    log.error("Unable to deliver message: exception=[ {} ], message=[ {} ]",
                            ex.getMessage(), ex.getFailedProducerRecord());
                }
            });
            return "ok";
        };
    }

    private Iterable<Header> createCustomHeaders() {
        return List.of(MessageIdUtils.createMessageIdHeader());
    }
}
