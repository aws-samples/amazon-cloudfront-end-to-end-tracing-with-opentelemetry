package com.aws.peach.infrastructure.configuration;

import com.aws.peach.infrastructure.redis.OrderData;
import com.aws.peach.infrastructure.redis.RedisInfras;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {RedisInfras.class})
public class RedisConfiguration {

    ObjectMapper redisObjectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule());
    }

    @Bean
    public RedisTemplate<String, OrderData> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        Jackson2JsonRedisSerializer<OrderData> serializer = new Jackson2JsonRedisSerializer<>(OrderData.class);
        serializer.setObjectMapper(redisObjectMapper());

        RedisTemplate<String, OrderData> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        return template;
    }
}
