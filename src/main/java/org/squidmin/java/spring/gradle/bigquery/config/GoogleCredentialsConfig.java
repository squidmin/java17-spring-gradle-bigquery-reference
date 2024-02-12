package org.squidmin.java.spring.gradle.bigquery.config;

import com.google.auth.oauth2.GoogleCredentials;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

@Configuration
@Getter
@Profile("!integration")
public class GoogleCredentialsConfig {

    @Bean
    public GoogleCredentials googleCredentials() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials.getApplicationDefault()
            .createScoped("https://www.googleapis.com/auth/cloud-platform");
        googleCredentials.refreshIfExpired();
        return googleCredentials;
    }

}
