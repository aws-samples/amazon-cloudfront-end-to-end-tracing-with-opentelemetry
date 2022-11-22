package com.aws.peach.configuration;

import com.aws.peach.application.Apps;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {
        PeachDomainContextConfig.class
})
@ComponentScan(basePackageClasses = {Apps.class})
public class PeachApplicationContextConfig {
}
