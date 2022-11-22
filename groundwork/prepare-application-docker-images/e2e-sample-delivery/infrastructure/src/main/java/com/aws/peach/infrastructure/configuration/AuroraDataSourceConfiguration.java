package com.aws.peach.infrastructure.configuration;

import com.aws.peach.domain.delivery.Delivery;
import com.aws.peach.domain.delivery.DeliveryItem;
import com.aws.peach.domain.delivery.Order;
import com.aws.peach.infrastructure.aurora.AuroraInfras;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackageClasses = {Delivery.class, Order.class, DeliveryItem.class})
@EnableJpaRepositories(basePackageClasses = {AuroraInfras.class})
@EnableAutoConfiguration
public class AuroraDataSourceConfiguration {
}