package org.squidmin.java.spring.gradle.bigquery.service;

import com.google.cloud.bigquery.InsertAllRequest;
import com.google.cloud.bigquery.TableResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.squidmin.java.spring.gradle.bigquery.CliConfig;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponse;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;
import org.squidmin.java.spring.gradle.bigquery.logger.Logger;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryUtil;
import org.squidmin.java.spring.gradle.bigquery.util.LoggerUtil;

import java.io.IOException;
import java.util.List;

//@Disabled
@Slf4j
public class BigQueryServiceEndToEndTest extends CliConfig {

    @Autowired
    private BigQueryService bigQueryService;

    @BeforeEach
    void beforeEach() {
        initialize();
        LoggerUtil.logRunConfig();
    }

    @Test
    void echoRunConfig() {
        LoggerUtil.logBqProperties(runEnvironment, LoggerUtil.ProfileOption.DEFAULT);
    }

    @Test
    void listDatasets() {
        bigQueryService.listDatasets();
    }

    @Test
    void createDataset() {
        bigQueryService.createDataset(GCP_DEFAULT_USER_DATASET);
    }

    @Test
    void deleteDataset() {
        bigQueryService.deleteDataset(GCP_DEFAULT_USER_PROJECT_ID, GCP_DEFAULT_USER_DATASET);
    }

    @Test
    void createTableWithDefaultSchema() {
        LoggerUtil.logBqProperties(runEnvironment, LoggerUtil.ProfileOption.ACTIVE);
        Assertions.assertTrue(
            bigQueryService.createTable(GCP_DEFAULT_USER_DATASET, GCP_DEFAULT_USER_TABLE)
        );
    }

    @Test
    void createTableWithCustomSchema() {
        LoggerUtil.logBqProperties(runEnvironment, LoggerUtil.ProfileOption.ACTIVE);
        Assertions.assertTrue(
            bigQueryService.createTable(
                GCP_DEFAULT_USER_DATASET,
                GCP_DEFAULT_USER_TABLE,
                BigQueryUtil.InlineSchemaTranslator.translate(schemaOverrideString, bigQueryConfig.getDataTypes())
            )
        );
    }

    @Test
    void deleteTable() {
        bigQueryService.deleteTable(
            GCP_DEFAULT_USER_PROJECT_ID,
            GCP_DEFAULT_USER_DATASET,
            GCP_DEFAULT_USER_TABLE
        );
    }

    @Test
    void insert() {
        List<InsertAllRequest.RowToInsert> rowsInserted = bigQueryService.insert(
            GCP_DEFAULT_USER_PROJECT_ID,
            GCP_DEFAULT_USER_DATASET,
            GCP_DEFAULT_USER_TABLE,
            BigQueryFunctionalTestFixture.DEFAULT_ROWS.get()
        );
        Assertions.assertTrue(0 < rowsInserted.size());
        rowsInserted.forEach(row -> Logger.log(String.valueOf(row), Logger.LogType.INFO));
    }

    @Test
    void query_givenQueryString_whenCallingBigQueryViaJdk_thenReturnValidResponse() {
        TableResult tableResult = bigQueryService.query(
            GCP_ADC_ACCESS_TOKEN,
            BigQueryFunctionalTestFixture.validQueryString(
                GCP_DEFAULT_USER_PROJECT_ID,
                GCP_DEFAULT_USER_DATASET,
                GCP_DEFAULT_USER_TABLE
            )
        );
        Assertions.assertTrue(0 < tableResult.getTotalRows());
    }

    @Test
    void query_givenQueryString_whenCallingBigQueryViaRestfulServices_thenReturnValidResponse() throws IOException {
        ResponseEntity<ExampleResponse> responseEntity = bigQueryService.query(BigQueryFunctionalTestFixture.validExampleRequest(), GCP_ADC_ACCESS_TOKEN);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertTrue(0 < responseEntity.getBody().getBody().size());
    }

}
