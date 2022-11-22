package com.aws.peach.configuration;

import com.aws.peach.infrastructure.configuration.api.PeachApiInfrastructureContextConfig;
import com.aws.peach.interfaces.api.Apis;
import com.aws.peach.interfaces.message.Messages;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {PeachApplicationContextConfig.class, PeachApiInfrastructureContextConfig.class})
@ComponentScan(basePackageClasses = {Apis.class, Messages.class})
public class PeachApiContextConfig {
}
