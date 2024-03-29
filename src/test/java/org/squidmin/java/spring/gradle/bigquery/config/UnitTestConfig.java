package org.squidmin.java.spring.gradle.bigquery.config;

import com.google.auth.oauth2.GoogleCredentials;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryTimeUtil;
import org.squidmin.java.spring.gradle.bigquery.util.TemplateCompiler;

import java.io.IOException;

@Configuration
@Profile("integration")
public class UnitTestConfig {

    @Value("${spring.cloud.gcp.config.credentials.location}")
    private String gcpSaKeyPath;

    private final String systemArgGcpSaKeyPath = System.getProperty("GOOGLE_APPLICATION_CREDENTIALS");

    @Value("${bigquery.application-default.project-id}")
    private String gcpProjectId;

    @Value("${bigquery.application-default.dataset}")
    private String gcpDataset;

    @Value("${bigquery.application-default.table}")
    private String gcpTable;

    @Value("${bigquery.uri.queries}")
    private String queryUri;

    private final BigQueryTimeUtil bigQueryTimeUtilMock = Mockito.mock(BigQueryTimeUtil.class);

    private BigQueryConfig bigQueryConfig;
    private final BigQueryConfig bigQueryConfigMock = Mockito.mock(BigQueryConfig.class);

    private final RestTemplate restTemplateMock = Mockito.mock(RestTemplate.class);

    private final TemplateCompiler templateCompilerMock = Mockito.mock(TemplateCompiler.class);

    @Bean
    @Qualifier("gcpSaKeyPath_unitTest")
    public String gcpSaKeyPath() { return gcpSaKeyPath; }

    @Bean
    @Qualifier("gcpProjectId_unitTest")
    public String gcpProjectId() {
        return gcpProjectId;
    }

    @Bean
    @Qualifier("gcpDataset_unitTest")
    public String gcpDataset() {
        return gcpDataset;
    }

    @Bean
    @Qualifier("gcpTable_unitTest")
    public String gcpTable() {
        return gcpTable;
    }

    @Bean
    @Qualifier("queryUri_unitTest")
    public String queryUri() {
        return queryUri;
    }

    private final GoogleCredentials googleCredentialsMock = Mockito.mock(GoogleCredentials.class);

    @Bean
    @Qualifier("bigQueryConfig_unitTest")
    public BigQueryConfig bigQueryConfig() throws IOException {
        bigQueryConfig = new BigQueryConfig(
//            gcpSaKeyPath,
            systemArgGcpSaKeyPath,
            gcpProjectId,
            gcpDataset,
            gcpTable,
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

    @Bean
    @Qualifier("googleCredentialsMock_unitTest")
    public GoogleCredentials googleCredentialsMock() {
        return googleCredentialsMock;
    }

}
