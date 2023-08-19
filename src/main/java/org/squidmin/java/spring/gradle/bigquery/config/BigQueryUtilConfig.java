package org.squidmin.java.spring.gradle.bigquery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryTimeUtil;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryUtil;
import org.squidmin.java.spring.gradle.bigquery.util.TemplateCompiler;

@Configuration
public class BigQueryUtilConfig {

    private BigQueryTimeUtil bigQueryTimeUtil;
    private TemplateCompiler templateCompiler;

    @Bean
    public BigQueryUtil bigQueryUtil() {
        return new BigQueryUtil(templateCompiler);
    }

    @Bean
    public BigQueryTimeUtil bigQueryTimeUtil() {
        bigQueryTimeUtil = new BigQueryTimeUtil();
        return bigQueryTimeUtil;
    }

    @Bean
    public TemplateCompiler templateCompiler() {
        templateCompiler = new TemplateCompiler(bigQueryTimeUtil);
        return templateCompiler;
    }

}
