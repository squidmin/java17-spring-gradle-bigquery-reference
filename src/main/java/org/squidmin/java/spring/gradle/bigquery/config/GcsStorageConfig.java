package org.squidmin.java.spring.gradle.bigquery.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

@Configuration
@Profile("!integration")
public class GcsStorageConfig {

    private final String projectId;

    private final GoogleCredentials googleCredentials;

    public GcsStorageConfig(@Value("${bigquery.application-default.project-id}") String projectId) throws IOException {
        this.projectId = projectId;
        googleCredentials = GoogleCredentials.getApplicationDefault()
            .createScoped("https://www.googleapis.com/auth/cloud-platform");
    }

    @Bean
    public Storage storage() {
        return StorageOptions.newBuilder()
            .setProjectId(projectId)
            .setCredentials(googleCredentials)
            .build().getService();
    }

}
