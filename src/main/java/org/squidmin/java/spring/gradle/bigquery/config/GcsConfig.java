package org.squidmin.java.spring.gradle.bigquery.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Google Cloud Storage (GCS) config
 */
@Configuration
@Getter
@Setter
@Slf4j
public class GcsConfig {

    private final String gcpDefaultUserProjectId;

    private final String gcsBucketName;

    private final String gcsFilename;

    private final String serviceAccountPath = System.getProperty("GCP_SA_KEY_PATH");

    private Storage storage;

    public GcsConfig(@Value("${bigquery.application-default.project-id}") String gcpDefaultUserProjectId,
                     @Value("${gcs.bucket.name}") String gcsBucketName,
                     @Value("${gcs.filename}") String gcsFilename) throws IOException {

        this.gcpDefaultUserProjectId = gcpDefaultUserProjectId;
        this.gcsBucketName = gcsBucketName;
        this.gcsFilename = gcsFilename;
        storage = StorageOptions.newBuilder()
            .setProjectId(gcpDefaultUserProjectId)
            .setCredentials(GoogleCredentials.fromStream(new FileInputStream(Paths.get(serviceAccountPath).toFile())))
            .build()
            .getService();

    }

}
