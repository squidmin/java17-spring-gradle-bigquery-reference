package org.squidmin.java.spring.gradle.bigquery;

import com.google.cloud.bigquery.BigQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.config.GcsConfig;
import org.squidmin.java.spring.gradle.bigquery.config.IntegrationTestConfig;
import org.squidmin.java.spring.gradle.bigquery.service.BigQueryService;
import org.squidmin.java.spring.gradle.bigquery.service.GcsService;

@SpringBootTest(classes = {BigQueryService.class, IntegrationTestConfig.class})
@ActiveProfiles("integration")
@Slf4j
public class IntegrationTest {

    @Autowired
    protected BigQueryConfig bigQueryConfig;

    @Autowired
    protected BigQuery bigQuery;

    @Autowired
    protected GcsConfig gcsConfig;

    @Autowired
    protected GcsService gcsService;

    @Autowired
    protected RestTemplate restTemplateMock;

}
