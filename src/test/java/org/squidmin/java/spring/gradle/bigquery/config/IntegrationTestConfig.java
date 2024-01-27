package org.squidmin.java.spring.gradle.bigquery.config;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.storage.Storage;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.squidmin.java.spring.gradle.bigquery.TestUtil;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;
import org.squidmin.java.spring.gradle.bigquery.service.GcsService;
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryHttpUtil;
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryTimeUtil;
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryUtil;
import org.squidmin.java.spring.gradle.bigquery.util.TemplateCompiler;

import java.io.IOException;

@Configuration
@ActiveProfiles("integration")
public class IntegrationTestConfig {

    private final String systemArgGcpSaKeyPath = System.getProperty("GCP_SA_KEY_PATH");
    private final String gcpAccessToken = System.getProperty("GCP_ACCESS_TOKEN");
    private final String gcpSaAccessToken = System.getProperty("GCP_SA_ACCESS_TOKEN");

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
    private final Storage gcsStorageMock = Mockito.mock(Storage.class);
    private GcsService gcsService;

    @Bean
    @Qualifier("gcpSaKeyPath_integrationTest")
    public String gcpSaKeyPath() {
        return gcpSaKeyPath;
    }

    @Bean
    @Qualifier("gcpDefaultUserProjectId_integrationTest")
    public String gcpDefaultUserProjectId() {
        return gcpDefaultProjectId;
    }

    @Bean
    @Qualifier("gcpDefaultUserDataset_integrationTest")
    public String gcpDefaultUserDataset() {
        return gcpDefaultDataset;
    }

    @Bean
    @Qualifier("gcpDefaultUserTable_integrationTest")
    public String gcpDefaultUserTable() {
        return gcpDefaultTable;
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
    @Qualifier("bigQuery_integrationTest")
    public BigQuery bigQuery() {
        return TestUtil.defaultBigQueryInstance(gcpSaKeyPath, gcpAccessToken, gcpSaAccessToken, gcpDefaultProjectId);
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
    public GcsConfig gcsConfig() {
        gcsConfig = new GcsConfig(gcpDefaultProjectId, gcsBucketName, gcsFilename, gcpSaKeyPath, gcsStorageMock);
        return gcsConfig;
    }

    @Bean
    @Qualifier("gcsService_integrationTest")
    public GcsService gcsService() {
        gcsService = new GcsService(new GcsConfig(gcpDefaultProjectId, gcsBucketName, gcsFilename, gcpSaKeyPath, gcsStorageMock));
        return gcsService;
    }

    @Bean
    @Qualifier("restTemplateMock_integrationTest")
    public RestTemplate restTemplateMock() {
        return Mockito.mock(RestTemplate.class);
    }

}
