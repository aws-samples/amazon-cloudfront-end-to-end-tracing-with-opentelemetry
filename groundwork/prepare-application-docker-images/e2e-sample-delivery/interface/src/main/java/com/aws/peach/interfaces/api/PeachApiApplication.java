package com.aws.peach.interfaces.api;

import com.aws.peach.configuration.PeachApiContextConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@Import(value = {PeachApiContextConfig.class})
public class PeachApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(PeachApiApplication.class, args);
	}
}
