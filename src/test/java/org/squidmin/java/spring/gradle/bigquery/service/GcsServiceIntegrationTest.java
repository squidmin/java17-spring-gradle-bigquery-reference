package org.squidmin.java.spring.gradle.bigquery.service;

import com.google.cloud.storage.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.squidmin.java.spring.gradle.bigquery.IntegrationTest;
import org.squidmin.java.spring.gradle.bigquery.config.GcsConfig;
import org.squidmin.java.spring.gradle.bigquery.config.UnitTestConfig;

@ActiveProfiles({"integration"})
@ContextConfiguration(classes = {UnitTestConfig.class})
@Slf4j
public class GcsServiceIntegrationTest extends IntegrationTest {

    private GcsService gcsService;

    @Autowired
    private GcsConfig gcsConfig;

    private String gcsBucketName;

    @BeforeEach
    void beforeEach() {
        gcsService = new GcsService(gcsConfig);
        gcsBucketName = gcsConfig.getGcsBucketName();
    }

    @Disabled
    @Test
    void createBucket() {
        Bucket bucket = null;
        if (!gcsService.bucketExists(gcsBucketName)) {
            bucket = gcsService.createBucket(gcsBucketName);
        }
        Assertions.assertNotNull(bucket);
    }

    @Test
    void bucketExists() {
        boolean bucketExists = gcsService.bucketExists(gcsBucketName);
        log.info("Bucket exists: {}", bucketExists);
    }

}
