package org.squidmin.java.spring.gradle.bigquery.config;

import com.google.cloud.storage.Storage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Google Cloud Storage (GCS) config
 */
@Configuration
@Getter
@Setter
@Profile("!integration")
@Slf4j
public class GcsConfig {

    private final String gcpDefaultProjectId;

    private final String gcsBucketName;

    private final String gcsFilename;

    private String gcpSaKeyPath;

    private Storage storage;

    public GcsConfig(@Value("${bigquery.application-default.project-id}") String gcpDefaultProjectId,
                     @Value("${gcs.bucket.name}") String gcsBucketName,
                     @Value("${gcs.filename}") String gcsFilename,
                     @Value("${spring.cloud.gcp.config.credentials.location}") String gcpSaKeyPath,
                     Storage storage) {

        this.gcpDefaultProjectId = gcpDefaultProjectId;
        this.gcsBucketName = gcsBucketName;
        this.gcsFilename = gcsFilename;
        final String systemArgSaKeyPath = System.getProperty("GCP_SA_KEY_PATH");
        this.gcpSaKeyPath = StringUtils.isNotEmpty(systemArgSaKeyPath) ? systemArgSaKeyPath : gcpSaKeyPath;
        this.storage = storage;

    }

}
