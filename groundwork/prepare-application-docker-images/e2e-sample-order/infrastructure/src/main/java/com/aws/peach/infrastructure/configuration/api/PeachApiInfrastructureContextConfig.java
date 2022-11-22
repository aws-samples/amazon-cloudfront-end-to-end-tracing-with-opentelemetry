package com.aws.peach.infrastructure.configuration.api;

import com.aws.peach.infrastructure.aurora.AuroraInfras;
import com.aws.peach.infrastructure.configuration.KafkaMessageConfiguration;
import com.aws.peach.infrastructure.configuration.RedisConfiguration;
import com.aws.peach.infrastructure.configuration.RestConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
        AuroraInfras.class,
        KafkaMessageConfiguration.class,
        RestConfig.class,
        RedisConfiguration.class
})
public class PeachApiInfrastructureContextConfig {
}
