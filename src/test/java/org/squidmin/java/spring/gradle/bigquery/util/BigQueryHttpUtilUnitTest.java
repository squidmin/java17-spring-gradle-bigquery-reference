package org.squidmin.java.spring.gradle.bigquery.util;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.squidmin.java.spring.gradle.bigquery.UnitTest;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponse;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryHttpUtil;

import java.io.IOException;

@Slf4j
public class BigQueryHttpUtilUnitTest extends UnitTest {

    @Value("${bigquery.application-default.project-id}")
    private String gcpDefaultProjectId;

    @Value("${bigquery.application-default.dataset}")
    private String gcpDefaultDataset;

    @Value("${bigquery.application-default.table}")
    private String gcpDefaultTable;

    @Value("${bigquery.uri.queries}")
    private String queryUri;

    private final BigQueryConfig bigQueryConfigMock = Mockito.mock(BigQueryConfig.class);

    private final RestTemplate restTemplateMock = Mockito.mock(RestTemplate.class);

    private BigQueryHttpUtil bigQueryHttpUtil;

    @BeforeEach
    void beforeEach() {
        UnitTest.setUp(
            bigQueryConfigMock,
            gcpDefaultProjectId,
            gcpDefaultDataset,
            gcpDefaultTable,
            queryUri
        );
        bigQueryHttpUtil = new BigQueryHttpUtil(restTemplateMock);
    }

    @AfterEach
    void afterEach() {
        Mockito.reset(restTemplateMock);
    }

    @Test
    void callBigQueryApi_givenValidParameters_thenReturnHttpStatusOk() throws IOException {
        String responseFixture = BigQueryFunctionalTestFixture.validBigQueryRestServiceResponse();
        HttpEntity<String> request = BigQueryFunctionalTestFixture.validHttpEntity(
            gcpDefaultProjectId,
            gcpDefaultDataset,
            gcpDefaultTable
        );
        bigQueryApiReturnsResponse(
            mapper.readValue(
                responseFixture,
                new TypeReference<>() {
                }
            ),
            HttpStatus.OK,
            restTemplateMock
        );
        ResponseEntity<ExampleResponse> responseEntity = bigQueryHttpUtil.callBigQueryApi(
            bigQueryConfigMock,
            request,
            mapper
        );
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}
