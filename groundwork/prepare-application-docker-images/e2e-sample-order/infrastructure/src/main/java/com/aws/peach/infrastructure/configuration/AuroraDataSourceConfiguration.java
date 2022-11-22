package com.aws.peach.infrastructure.configuration;

import com.aws.peach.domain.order.entity.Orders;
import com.aws.peach.domain.order.vo.OrderLine;
import com.aws.peach.domain.product.entity.Product;
import com.aws.peach.infrastructure.aurora.AuroraInfras;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackageClasses = {Orders.class, Product.class, OrderLine.class})
@EnableJpaRepositories(basePackageClasses = {AuroraInfras.class})
@EnableAutoConfiguration
public class AuroraDataSourceConfiguration {
}