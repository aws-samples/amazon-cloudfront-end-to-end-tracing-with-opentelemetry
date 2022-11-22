package com.aws.peach.infrastructure.configuration;

import com.aws.peach.domain.delivery.DeliveryChangeMessage;
import com.aws.peach.domain.support.MessageProducer;
import com.aws.peach.infrastructure.kafka.KafkaInfras;
import com.aws.peach.infrastructure.kafka.KafkaMessageProducerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@ComponentScan(basePackageClasses = {KafkaInfras.class})
public class KafkaMessageConfiguration {
    private final KafkaMessageProducerFactory producerFactory;

    public KafkaMessageConfiguration(final KafkaMessageProducerFactory producerFactory) {
        this.producerFactory = producerFactory;
    }

    @Bean
    public MessageProducer<String, DeliveryChangeMessage> deliveryChangeMessageProducer(final KafkaTemplate<String, DeliveryChangeMessage> kafkaTemplate,
                                                                                       @Value("${kafka.topic.delivery-change}") final String topic) {
        return this.producerFactory.create(kafkaTemplate, topic);
    }
}
