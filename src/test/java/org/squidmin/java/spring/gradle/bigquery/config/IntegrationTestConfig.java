package org.squidmin.java.spring.gradle.bigquery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class IntegrationTestConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
