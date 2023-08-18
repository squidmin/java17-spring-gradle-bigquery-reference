package org.squidmin.java.spring.gradle.bigquery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Java17SpringGradleBigQueryReferenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Java17SpringGradleBigQueryReferenceApplication.class, args);
    }

}
