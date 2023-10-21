package org.squidmin.java.spring.gradle.bigquery.config;

import com.google.cloud.bigquery.BigQuery;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.squidmin.java.spring.gradle.bigquery.TestUtil;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;
import org.squidmin.java.spring.gradle.bigquery.logger.Logger;
import org.squidmin.java.spring.gradle.bigquery.service.GcsService;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryHttpUtil;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryTimeUtil;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryUtil;
import org.squidmin.java.spring.gradle.bigquery.util.TemplateCompiler;

import java.io.IOException;

@Configuration
@ActiveProfiles("integration")
public class IntegrationTestConfig {

    private final String systemArgGcpSaKeyPath = System.getProperty("GCP_SA_KEY_PATH");
    private final String gcpAdcAccessToken = System.getProperty("GCP_ADC_ACCESS_TOKEN");
    private final String gcpSaAccessToken = System.getProperty("GCP_SA_ACCESS_TOKEN");

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

    @Value("${gcs.bucket.name}")
    private String gcsBucketName;

    @Value("${gcs.filename}")
    private String gcsFilename;

    private RestTemplate restTemplate;

    private BigQueryConfig bigQueryConfig;

    private BigQueryUtil bigQueryUtil;
    private BigQueryHttpUtil bigQueryHttpUtil;
    private BigQueryTimeUtil bigQueryTimeUtil;

    private GcsConfig gcsConfig;
    private GcsService gcsService;

    @Bean
    @Qualifier("gcpSaKeyPath_integrationTest")
    public String gcpSaKeyPath() {
//        Logger.log("gcpSaKeyPath == " + gcpSaKeyPath, Logger.LogType.CYAN);
        return gcpSaKeyPath;
    }

    @Bean
    @Qualifier("gcpDefaultUserProjectId_integrationTest")
    public String gcpDefaultUserProjectId() {
        return gcpDefaultUserProjectId;
    }

    @Bean
    @Qualifier("gcpDefaultUserDataset_integrationTest")
    public String gcpDefaultUserDataset() {
        return gcpDefaultUserDataset;
    }

    @Bean
    @Qualifier("gcpDefaultUserTable_integrationTest")
    public String gcpDefaultUserTable() {
        return gcpDefaultUserTable;
    }

    @Bean
    @Qualifier("gcpSaProjectId_integrationTest")
    public String gcpSaProjectId() {
        return gcpSaProjectId;
    }

    @Bean
    @Qualifier("gcpSaDataset_integrationTest")
    public String gcpSaDataset() {
        return gcpSaDataset;
    }

    @Bean
    @Qualifier("gcpSaTable_integrationTest")
    public String gcpSaTable() {
        return gcpSaTable;
    }

    @Bean
    @Qualifier("queryUri_integrationTest")
    public String queryUri() {
        return queryUri;
    }

    @Bean
    @Qualifier("bigQueryConfig_integrationTest")
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
    @Qualifier("bigQuery_integrationTest")
    public BigQuery bigQuery() {
        return TestUtil.defaultBigQueryInstance(gcpSaKeyPath, gcpAdcAccessToken, gcpSaAccessToken, gcpDefaultUserProjectId);
    }

    @Bean
    @Qualifier("restTemplate_integrationTest")
    public RestTemplate restTemplate() {
        restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Bean
    @Qualifier("bigQueryUtil_integrationTest")
    public BigQueryUtil bigQueryUtil() {
        bigQueryUtil = new BigQueryUtil(new TemplateCompiler(new BigQueryTimeUtil()));
        return bigQueryUtil;
    }

    @Bean
    @Qualifier("bigQueryHttpUtil_integrationTest")
    public BigQueryHttpUtil bigQueryHttpUtil() {
        bigQueryHttpUtil = new BigQueryHttpUtil(restTemplate);
        return bigQueryHttpUtil;
    }

    @Bean
    @Qualifier("bigQueryTimeUtil_integrationTest")
    public BigQueryTimeUtil bigQueryTimeUtil() {
        bigQueryTimeUtil = new BigQueryTimeUtil();
        return bigQueryTimeUtil;
    }

    @Bean
    @Qualifier("gcsConfig_integrationTest")
    public GcsConfig gcsConfig() throws IOException {
        gcsConfig = new GcsConfig(gcpDefaultUserProjectId, gcsBucketName, gcsFilename, gcpSaKeyPath);
        return gcsConfig;
    }

    @Bean
    @Qualifier("gcsService_integrationTest")
    public GcsService gcsService() throws IOException {
        gcsService = new GcsService(new GcsConfig(gcpDefaultUserProjectId, gcsBucketName, gcsFilename, gcpSaKeyPath));
        return gcsService;
    }

    @Bean
    @Qualifier("restTemplateMock_integrationTest")
    public RestTemplate restTemplateMock() {
        return Mockito.mock(RestTemplate.class);
    }

}
