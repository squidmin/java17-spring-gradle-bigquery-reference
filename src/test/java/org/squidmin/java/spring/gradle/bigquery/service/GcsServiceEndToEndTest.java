package org.squidmin.java.spring.gradle.bigquery.service;

import com.google.cloud.storage.Bucket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.squidmin.java.spring.gradle.bigquery.CliConfig;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponseItem;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;
import org.squidmin.java.spring.gradle.bigquery.util.LoggerUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class GcsServiceEndToEndTest extends CliConfig {

    @BeforeEach
    void beforeEach() {
        initialize();
        LoggerUtil.logRunConfig();
    }

    @Test
    void createBucket_givenValidParameters_thenCreateBucket() {
        Bucket bucket = gcsService.createBucket("lofty_root_test_bucket");
        Assertions.assertNotNull(bucket);
    }

    @Test
    void upload_givenValidParameters_thenUploadRows() throws IOException {
        List<ExampleResponseItem> responseItems = BigQueryFunctionalTestFixture.validExampleResponseItems().getBody();
        URL upload = gcsService.upload(responseItems);
        Assertions.assertTrue(upload.toString().contains("https://storage.googleapis.com/lofty_root_test_bucket/test_large_response_upload.csv?X-Goog-Algorithm="));
    }

}