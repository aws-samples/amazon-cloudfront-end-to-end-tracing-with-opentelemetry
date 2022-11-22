package com.aws.peach.configuration;

import com.aws.peach.domain.Domains;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {})
@ComponentScan(basePackageClasses = {Domains.class})
public class PeachDomainContextConfig {
}
