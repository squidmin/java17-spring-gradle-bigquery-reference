package org.squidmin.java.spring.gradle.bigquery.actuator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.controller.BigQueryController;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;
import org.squidmin.java.spring.gradle.bigquery.repository.ExampleRepositoryImpl;
import org.squidmin.java.spring.gradle.bigquery.service.BigQueryService;
import org.squidmin.java.spring.gradle.bigquery.service.GcsService;
import org.squidmin.java.spring.gradle.bigquery.util.TemplateCompiler;
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryHttpUtil;
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryTimeUtil;
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryUtil;

import java.io.IOException;

@ActiveProfiles({"integration"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class ActuatorTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void health() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            "http://localhost:" + port + "/actuator/health",
            String.class
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void info() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            "http://localhost:" + port + "/actuator/info",
            String.class
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @TestConfiguration
    public static class TestConfig {

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

        private BigQueryConfig bigQueryConfig;

        private BigQueryUtil bigQueryUtil;
        private BigQueryHttpUtil bigQueryHttpUtil;

        private final GcsService gcsServiceMock = Mockito.mock(GcsService.class);

        @Bean
        public BigQueryUtil bigQueryUtil() {
            bigQueryUtil = new BigQueryUtil(new TemplateCompiler(new BigQueryTimeUtil()));
            return bigQueryUtil;
        }

        @Bean
        public BigQueryHttpUtil bigQueryHttpUtil() {
            bigQueryHttpUtil = new BigQueryHttpUtil(new RestTemplate());
            return bigQueryHttpUtil;
        }

        @Bean
        public BigQueryTimeUtil bigQueryTimeUtil() {
            return new BigQueryTimeUtil();
        }

        @Bean
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

    }

}
