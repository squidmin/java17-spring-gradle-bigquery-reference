package org.squidmin.java.spring.gradle.bigquery.config;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryTimeUtil;
import org.squidmin.java.spring.gradle.bigquery.util.TemplateCompiler;

import java.io.IOException;

@Configuration
@Profile("integration")
public class UnitTestConfig {

    @Value("${spring.cloud.gcp.config.credentials.location}")
    private String gcpSaKeyPath;

    @Value("${bigquery.application-default.project-id}")
    private String gcpDefaultUserProjectId;

    @Value("${bigquery.application-default.dataset}")
    private String gcpDefaultUserDataset;

    @Value("${bigquery.application-default.table}")
    private String gcpDefaultUserTable;

    @Value("${bigquery.service-account.project-id}")
    private String gcpSaProjectId;

    @Value("${bigquery.service-account.dataset}")
    private String gcpSaDataset;

    @Value("${bigquery.service-account.table}")
    private String gcpSaTable;

    @Value("${bigquery.uri.queries}")
    private String queryUri;

    private final BigQueryTimeUtil bigQueryTimeUtilMock = Mockito.mock(BigQueryTimeUtil.class);

    private BigQueryConfig bigQueryConfig;
    private BigQueryConfig bigQueryConfigMock = Mockito.mock(BigQueryConfig.class);

    private final RestTemplate restTemplateMock = Mockito.mock(RestTemplate.class);

    private final TemplateCompiler templateCompilerMock = Mockito.mock(TemplateCompiler.class);

    @Bean
    @Qualifier("gcpSaKeyPath_unitTest")
    public String gcpSaKeyPath() { return gcpSaKeyPath; }

    @Bean
    @Qualifier("gcpDefaultUserProjectId_unitTest")
    public String gcpDefaultUserProjectId() {
        return gcpDefaultUserProjectId;
    }

    @Bean
    @Qualifier("gcpDefaultUserDataset_unitTest")
    public String gcpDefaultUserDataset() {
        return gcpDefaultUserDataset;
    }

    @Bean
    @Qualifier("gcpDefaultUserTable_unitTest")
    public String gcpDefaultUserTable() {
        return gcpDefaultUserTable;
    }

    @Bean
    @Qualifier("gcpSaProjectId_unitTest")
    public String gcpSaProjectId() {
        return gcpSaProjectId;
    }

    @Bean
    @Qualifier("gcpSaDataset_unitTest")
    public String gcpSaDataset() {
        return gcpSaDataset;
    }

    @Bean
    @Qualifier("gcpSaTable_unitTest")
    public String gcpSaTable() {
        return gcpSaTable;
    }

    @Bean
//    @Qualifier("queryUri_unitTest")
    public String queryUri() {
        return queryUri;
    }

    @Bean
    @Qualifier("bigQueryConfig_unitTest")
    public BigQueryConfig bigQueryConfig() throws IOException {
        bigQueryConfig = new BigQueryConfig(
            gcpSaKeyPath,
            gcpDefaultUserProjectId,
            gcpDefaultUserDataset,
            gcpDefaultUserTable,
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
    @Qualifier("bigQueryConfigMock_unitTest")
    public BigQueryConfig bigQueryConfigMock() {
        return bigQueryConfigMock;
    }

    @Bean
    @Qualifier("templateCompilerMock_unitTest")
    public TemplateCompiler templateCompilerMock() {
        return templateCompilerMock;
    }

    @Bean
    @Qualifier("bigQueryTimeUtilMock_unitTest")
    public BigQueryTimeUtil bigQueryTimeUtilMock() {
        return bigQueryTimeUtilMock;
    }

    @Bean
    @Qualifier("restTemplateMock_unitTest")
    public RestTemplate restTemplateMock() {
        return restTemplateMock;
    }

}
