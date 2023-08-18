package org.squidmin.java.spring.gradle.bigquery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryTimeUtil;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryUtil;
import org.squidmin.java.spring.gradle.bigquery.util.TemplateCompiler;

@Configuration
public class BigQueryUtilConfig {

    @Bean
    public BigQueryUtil bigQueryUtil() {
        return new BigQueryUtil(new TemplateCompiler(new BigQueryTimeUtil()));
    }

}
