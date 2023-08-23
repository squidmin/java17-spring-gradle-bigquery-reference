package org.squidmin.java.spring.gradle.bigquery.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.config.UnitTestConfig;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponse;
import org.squidmin.java.spring.gradle.bigquery.dto.bigquery.BigQueryRestServiceResponse;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;

import java.io.IOException;

@SpringBootTest(classes = {UnitTestConfig.class})
@Slf4j
public class BigQueryHttpUtilUnitTest {

    @Autowired
    private String gcpDefaultUserProjectId;
    @Autowired
    private String gcpDefaultUserDataset;
    @Autowired
    private String gcpDefaultUserTable;
    @Autowired
    private String queryUri;

    private final BigQueryConfig bigQueryConfigMock = Mockito.mock(BigQueryConfig.class);

    private final RestTemplate restTemplateMock = Mockito.mock(RestTemplate.class);

    private final ObjectMapper mapper = new ObjectMapper();

    private BigQueryHttpUtil bigQueryHttpUtil;

    @BeforeEach
    void beforeEach() {
        setUpBigQueryConfigMock();
        bigQueryHttpUtil = new BigQueryHttpUtil(restTemplateMock);
    }

    @AfterEach
    void afterEach() {
        Mockito.reset(restTemplateMock);
    }

    @Test
    void callBigQueryApi_givenValidParameters_thenReturnHttpStatusOk() throws IOException {
        String responseFixture = BigQueryFunctionalTestFixture.validBigQueryRestServiceResponse();
        HttpEntity<String> request = new HttpEntity<>(
            BigQueryFunctionalTestFixture.validQueryString(
                gcpDefaultUserProjectId,
                gcpDefaultUserDataset,
                gcpDefaultUserTable
            ),
            BigQueryFunctionalTestFixture.validHttpHeaders()
        );
        bigQueryApiReturnsResponse(
            mapper.readValue(
                responseFixture,
                new TypeReference<>() {
                }),
            HttpStatus.OK
        );
        ResponseEntity<ExampleResponse> responseEntity = bigQueryHttpUtil.callBigQueryApi(
            bigQueryConfigMock,
            request,
            mapper
        );
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    private void bigQueryApiReturnsResponse(BigQueryRestServiceResponse response, HttpStatus httpStatus)
        throws JsonProcessingException {

        Mockito.when(
            restTemplateMock.postForEntity(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpEntity.class),
                ArgumentMatchers.eq(String.class)
            )
        ).thenReturn(new ResponseEntity<>(mapper.writeValueAsString(response), httpStatus));

    }

    private void setUpBigQueryConfigMock() {
        Mockito.when(bigQueryConfigMock.getGcpDefaultUserProjectId()).thenReturn(gcpDefaultUserProjectId);
        Mockito.when(bigQueryConfigMock.getGcpDefaultUserDataset()).thenReturn(gcpDefaultUserDataset);
        Mockito.when(bigQueryConfigMock.getGcpDefaultUserTable()).thenReturn(gcpDefaultUserTable);
        Mockito.when(bigQueryConfigMock.getQueryUri()).thenReturn(queryUri);

        Mockito.when(bigQueryConfigMock.getSelectFieldsDefault())
            .thenReturn(BigQueryFunctionalTestFixture.validSelectFieldsDefault());
        Mockito.when(bigQueryConfigMock.getWhereFieldsDefault())
            .thenReturn(BigQueryFunctionalTestFixture.validWhereFieldsDefault());

        Mockito.when(bigQueryConfigMock.getExclusions()).thenReturn(BigQueryFunctionalTestFixture.validExclusions());
    }

}
