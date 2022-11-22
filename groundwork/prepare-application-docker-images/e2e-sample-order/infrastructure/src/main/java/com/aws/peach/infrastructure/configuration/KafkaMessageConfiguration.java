package com.aws.peach.infrastructure.configuration;

import com.aws.peach.domain.delivery.DeliveryChangeMessage;
import com.aws.peach.domain.support.MessageConsumer;
import com.aws.peach.infrastructure.kafka.KafkaInfras;
import com.aws.peach.infrastructure.kafka.KafkaMessageListenerContainerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackageClasses = {KafkaInfras.class})
public class KafkaMessageConfiguration {
    private final KafkaMessageListenerContainerFactory listenerContainerFactory;

    @Bean
    public KafkaMessageListenerContainer<String, DeliveryChangeMessage> deliveryChangeMessageListenerContainer(
            @Value("${kafka.topic.delivery-change}") final String topic,
            final MessageConsumer<DeliveryChangeMessage> messageConsumer,
            final ConsumerFactory<String, DeliveryChangeMessage> consumerFactory,
            final KafkaTemplate<String, DeliveryChangeMessage> kafkaTemplate) {

        return this.listenerContainerFactory.create(topic, messageConsumer, consumerFactory, kafkaTemplate);
    }
}
