package com.aws.peach.infrastructure.configuration;

import com.aws.peach.infrastructure.rest.RestInfras;
import com.aws.peach.infrastructure.rest.api.DeliveryServiceAPI;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
@ComponentScan(basePackageClasses = {RestInfras.class})
public class RestConfig {

    @Value("${application.delivery.base-url}")
    private String BASE_URL_DELIVERY_SERVICE;

    private ConnectionPool connectionPool() {
        // re-using connections for 5 minutes
        // keeps 100 idle connections at most
        return new ConnectionPool(100, 5, TimeUnit.MINUTES);
    }

    @Bean
    OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectionPool(connectionPool())
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);
        return builder.build();
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule());
    }

    @Bean
    Converter.Factory converterFactory(ObjectMapper objectMapper) {
        return JacksonConverterFactory.create(objectMapper);
    }

    @Bean
    Retrofit deliveryServiceRetrofit(OkHttpClient client, Converter.Factory converterFactory) {
        return new Retrofit.Builder().baseUrl(BASE_URL_DELIVERY_SERVICE)
                .addConverterFactory(converterFactory)
                .client(client)
                .build();
    }

    @Bean
    public DeliveryServiceAPI deliveryServiceAPI(Retrofit deliveryServiceRetrofit) {
        return deliveryServiceRetrofit.create(DeliveryServiceAPI.class);
    }
}
