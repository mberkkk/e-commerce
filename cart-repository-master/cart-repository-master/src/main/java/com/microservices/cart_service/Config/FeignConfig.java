// Config/FeignConfig.java - Spring Boot 3.x için
package com.microservices.cart_service.Config;

import feign.Logger;
import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
                10000, TimeUnit.MILLISECONDS, // connect timeout
                30000, TimeUnit.MILLISECONDS, // read timeout
                true // followRedirects
        );
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000L, 3000L, 3);
    }
}