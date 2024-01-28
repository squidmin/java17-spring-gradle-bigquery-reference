package org.squidmin.java.spring.gradle.bigquery.config;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.squidmin.java.spring.gradle.bigquery.controller.BigQueryController;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;
import org.squidmin.java.spring.gradle.bigquery.repository.ExampleRepositoryImpl;
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryUtil;

import java.io.IOException;

@Configuration
@ActiveProfiles("integration")
public class ControllerUnitTestConfig {

    private final String googleApplicationCredentialsPath = System.getProperty("GOOGLE_APPLICATION_CREDENTIALS");

    @Value("${spring.cloud.gcp.config.credentials.location}")
    private String gcpSaKeyPath;

    @Value("${bigquery.application-default.project-id}")
    private String gcpDefaultProjectId;

    @Value("${bigquery.application-default.dataset}")
    private String gcpDefaultDataset;

    @Value("${bigquery.application-default.table}")
    private String gcpDefaultTable;

    @Value("${bigquery.service-account.project-id}")
    private String gcpSaProjectId;

    @Value("${bigquery.service-account.dataset}")
    private String gcpSaDataset;

    @Value("${bigquery.service-account.table}")
    private String gcpSaTable;

    @Value("${bigquery.uri.queries}")
    private String queryUri;

    private final BigQueryConfig bigQueryConfigMock = Mockito.mock(BigQueryConfig.class);

    private final ExampleRepositoryImpl exampleRepositoryMock = Mockito.mock(ExampleRepositoryImpl.class);

    private final BigQueryUtil bigQueryUtilMock = Mockito.mock(BigQueryUtil.class);

    private final RestTemplate restTemplateMock = Mockito.mock(RestTemplate.class);

    private BigQueryConfig bigQueryConfig;

    @Bean
    public String gcpSaKeyPath() {
        return gcpSaKeyPath;
    }

    @Bean
    public String gcpDefaultProjectId() {
        return gcpDefaultProjectId;
    }

    @Bean
    public String gcpDefaultDataset() {
        return gcpDefaultDataset;
    }

    @Bean
    public String gcpDefaultTable() {
        return gcpDefaultTable;
    }

    @Bean
    public String gcpSaProjectId() {
        return gcpSaProjectId;
    }

    @Bean
    public String gcpSaDataset() {
        return gcpSaDataset;
    }

    @Bean
    public String gcpSaTable() {
        return gcpSaTable;
    }

    @Bean
    public String queryUri() {
        return queryUri;
    }

    @Bean
    @Qualifier("bigQueryConfigMock_controllerUnitTest")
    public BigQueryConfig bigQueryConfigMock() {
        return bigQueryConfigMock;
    }

    @Bean
    @Qualifier("bigQueryConfig_controllerUnitTest")
    public BigQueryConfig bigQueryConfig() throws IOException {
        bigQueryConfig = new BigQueryConfig(
//            gcpSaKeyPath,
            googleApplicationCredentialsPath,
            gcpDefaultProjectId,
            gcpDefaultDataset,
            gcpDefaultTable,
            gcpSaProjectId,
            gcpSaDataset,
            gcpSaTable,
            queryUri,
            BigQueryFunctionalTestFixture.validSchemaDefault(),
            BigQueryFunctionalTestFixture.validDataTypes(),
            BigQueryFunctionalTestFixture.validSelectFieldsDefault(),
            BigQueryFunctionalTestFixture.validWhereFieldsDefault(),
            BigQueryFunctionalTestFixture.validExclusions(),
            false
        );
        return bigQueryConfig;
    }

    @Bean
    @Qualifier("${exampleRepositoryMock_controllerUnitTest}")
    public ExampleRepositoryImpl exampleRepositoryMock() {
        return exampleRepositoryMock;
    }

    @Bean
    @Qualifier("${bigQueryUtilMock_controllerUnitTest}")
    public BigQueryUtil bigQueryUtilMock() {
        return bigQueryUtilMock;
    }

    @Bean
    @Qualifier("reatTemplateMock_controllerUnitTest")
    public RestTemplate restTemplateMock() {
        return restTemplateMock;
    }

    @Bean
    @Qualifier("bigQueryController_controllerUnitTest")
    public BigQueryController bigQueryController() {
        return new BigQueryController(exampleRepositoryMock);
    }

}
