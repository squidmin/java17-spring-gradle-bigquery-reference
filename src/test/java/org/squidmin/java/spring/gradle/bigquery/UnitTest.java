package org.squidmin.java.spring.gradle.bigquery;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.config.UnitTestConfig;
import org.squidmin.java.spring.gradle.bigquery.dto.bigquery.BigQueryRestServiceResponse;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;

@SpringBootTest(classes = {UnitTestConfig.class})
public abstract class UnitTest {

    protected static final ObjectMapper mapper = new ObjectMapper();

    protected static void bigQueryApiReturnsResponse(BigQueryRestServiceResponse response,
                                                     HttpStatus httpStatus,
                                                     RestTemplate restTemplateMock) throws JsonProcessingException {

        Mockito.when(
            restTemplateMock.postForEntity(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpEntity.class),
                ArgumentMatchers.eq(String.class)
            )
        ).thenReturn(new ResponseEntity<>(mapper.writeValueAsString(response), httpStatus));

    }

    protected static void setUp(BigQueryConfig bigQueryConfigMock,
                                String gcpDefaultUserProjectId,
                                String gcpDefaultUserDataset,
                                String gcpDefaultUserTable,
                                String queryUri) {

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
