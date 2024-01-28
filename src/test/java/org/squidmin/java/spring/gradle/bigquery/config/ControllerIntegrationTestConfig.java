package org.squidmin.java.spring.gradle.bigquery.config;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.squidmin.java.spring.gradle.bigquery.TestUtil;
import org.squidmin.java.spring.gradle.bigquery.controller.BigQueryController;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;
import org.squidmin.java.spring.gradle.bigquery.repository.ExampleRepositoryImpl;
import org.squidmin.java.spring.gradle.bigquery.service.BigQueryService;
import org.squidmin.java.spring.gradle.bigquery.service.GcpTokenService;
import org.squidmin.java.spring.gradle.bigquery.service.GcsService;
import org.squidmin.java.spring.gradle.bigquery.util.TemplateCompiler;
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryHttpUtil;
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryTimeUtil;
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryUtil;

import java.io.IOException;

@Configuration
@ActiveProfiles("integration")
@Slf4j
public class ControllerIntegrationTestConfig {

    private final String systemArgGcpSaKeyPath = System.getProperty("GCP_SA_KEY_PATH");
    private final String gcpAccessToken = System.getProperty("GCP_ACCESS_TOKEN");
    private final String gcpSaAccessToken = System.getProperty("GCP_SA_ACCESS_TOKEN");

    @Value("${gcp.service-account}")
    private String gcpServiceAccount;

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
    private final RestTemplate restTemplateMock = Mockito.mock(RestTemplate.class);

    private BigQueryConfig bigQueryConfig;

    private BigQueryUtil bigQueryUtil;
    private BigQueryHttpUtil bigQueryHttpUtil;
    private BigQueryHttpUtil bigQueryHttpUtilMock = Mockito.mock(BigQueryHttpUtil.class);
    private BigQueryTimeUtil bigQueryTimeUtil;

    private GcpTokenService gcpTokenService;

    private final GcsService gcsServiceMock = Mockito.mock(GcsService.class);
    private final Storage gcsStorageMock = Mockito.mock(Storage.class);

    @Autowired
    private WebApplicationContext context;

    @Bean
    @Qualifier("gcpSaKeyPath_controllerIntegrationTest")
    public String gcpSaKeyPath() {
//        Logger.log("gcpSaKeyPath == " + gcpSaKeyPath, Logger.LogType.CYAN);
        return StringUtils.isEmpty(systemArgGcpSaKeyPath) ? gcpSaKeyPath : systemArgGcpSaKeyPath;
    }

    @Bean
    @Qualifier("gcpDefaultProjectId_controllerIntegrationTest")
    public String gcpDefaultProjectId() {
        return gcpDefaultProjectId;
    }

    @Bean
    @Qualifier("gcpDefaultDataset_controllerIntegrationTest")
    public String gcpDefaultDataset() {
        return gcpDefaultDataset;
    }

    @Bean
    @Qualifier("gcpDefaultTable_controllerIntegrationTest")
    public String gcpDefaultTable() {
        return gcpDefaultTable;
    }

    @Bean
    @Qualifier("gcpSaProjectId_controllerIntegrationTest")
    public String gcpSaProjectId() {
        return gcpSaProjectId;
    }

    @Bean
    @Qualifier("gcpSaDataset_controllerIntegrationTest")
    public String gcpSaDataset() {
        return gcpSaDataset;
    }

    @Bean
    @Qualifier("gcpSaTable_controllerIntegrationTest")
    public String gcpSaTable() {
        return gcpSaTable;
    }

    @Bean
    @Qualifier("queryUri_controllerIntegrationTest")
    public String queryUri() {
        return queryUri;
    }

    @Bean
    @Qualifier("bigQueryConfig_controllerIntegrationTest")
    public BigQueryConfig bigQueryConfig() throws IOException {
        bigQueryConfig = new BigQueryConfig(
            StringUtils.isEmpty(systemArgGcpSaKeyPath) ? gcpSaKeyPath : systemArgGcpSaKeyPath,
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
    @Qualifier("bigQuery_controllerIntegrationTest")
    public BigQuery bigQuery() {
        return TestUtil.defaultBigQueryInstance(
            StringUtils.isEmpty(systemArgGcpSaKeyPath) ? gcpSaKeyPath : systemArgGcpSaKeyPath,
            gcpAccessToken,
            gcpSaAccessToken,
            gcpDefaultProjectId
        );
    }

    @Bean
    @Qualifier("restTemplate_controllerIntegrationTest")
    public RestTemplate restTemplate() {
        restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Bean
    @Qualifier("bigQueryUtil_controllerIntegrationTest")
    public BigQueryUtil bigQueryUtil() {
        bigQueryUtil = new BigQueryUtil(new TemplateCompiler(new BigQueryTimeUtil()));
        return bigQueryUtil;
    }

    @Bean
    @Qualifier("bigQueryHttpUtil_controllerIntegrationTest")
    public BigQueryHttpUtil bigQueryHttpUtil() {
        bigQueryHttpUtil = new BigQueryHttpUtil(restTemplateMock);
        return bigQueryHttpUtil;
    }

    @Bean
    @Qualifier("bigQueryTimeUtil_controllerIntegrationTest")
    public BigQueryTimeUtil bigQueryTimeUtil() {
        bigQueryTimeUtil = new BigQueryTimeUtil();
        return bigQueryTimeUtil;
    }

    @Bean
    @Qualifier("gcpTokenGeneratorService_controllerIntegrationTest")
    public GcpTokenService gcpTokenGeneratorService() throws IOException {
        gcpTokenService = new GcpTokenService(gcpServiceAccount, new RestTemplate());
        return gcpTokenService;
    }

    @Bean
    @Qualifier("bigQueryController_controllerIntegrationTest")
    public BigQueryController bigQueryController() {
        return new BigQueryController(
            new ExampleRepositoryImpl(
                new BigQueryService(
                    1,
                    bigQueryUtil,
                    bigQueryHttpUtil,
                    bigQueryConfig,
                    gcsServiceMock
                )
            )
        );
    }

    @Bean
    @Qualifier("gcsConfig_controllerIntegrationTest")
    public GcsConfig gcsConfig() {
        return new GcsConfig(gcpDefaultProjectId, gcsBucketName, gcsFilename, gcpSaKeyPath, gcsStorageMock);
    }

    @Bean
    @Qualifier("gcsServiceMock_controllerIntegrationnTest")
    public GcsService gcsServiceMock() {
        return gcsServiceMock;
    }

    @Bean
    @Qualifier("restTemplateMock_controllerIntegrationTest")
    public RestTemplate restTemplateMock() {
        return restTemplateMock;
    }

}
