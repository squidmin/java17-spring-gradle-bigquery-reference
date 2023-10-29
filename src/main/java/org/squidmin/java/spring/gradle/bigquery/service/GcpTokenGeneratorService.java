package org.squidmin.java.spring.gradle.bigquery.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ImpersonatedCredentials;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;

@Service
@Slf4j
public class GcpTokenGeneratorService {

    private static final String METADATA_SERVER_BASE_URL =
        "http://metadata.google.internal/computeMetadata/v1/instance/service-accounts/default/identity";

    private static final String AUDIENCE = "java17-spring-gradle-bigquery-reference";

    private static final String METADATA_FLAVOR = "Metadata-Flavor";

    private final String serviceAccount;

    private final GoogleCredentials googleCredentials;

    private final RestTemplate restTemplate;

    public GcpTokenGeneratorService(@Value("${gcp.service-account}") String serviceAccount,
                                    GoogleCredentials googleCredentials,
                                    RestTemplate restTemplate) throws IOException {

        this.serviceAccount = serviceAccount;

        this.googleCredentials = googleCredentials;
        googleCredentials.refreshIfExpired();

        this.restTemplate = restTemplate;

    }


    /**
     * Audience:
     * The intended recipient of the token. A string that is defined by the person or service that generates the token
     * and is used to identify the intended recipients.
     * When a service receives an identity token, it checks the `aud` claim in the token to ensure that it matches
     * the service's own identifier. If the `aud` claim does not match the service's identifier, the service should
     * reject the token.
     * This mechanism helps prevent token redirection, where a token intended for one service is presented to another service.
     */
    public String generateIdentityToken() {
        String metadataServerUrl = METADATA_SERVER_BASE_URL + "?audience=" + AUDIENCE;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(METADATA_FLAVOR, "Google");

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(metadataServerUrl));
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
        return Strings.EMPTY;
    }

    public String generateAccessToken() {
        String accessToken = Strings.EMPTY;
        try {
            accessToken = generateImpersonatedCredentials(googleCredentials, serviceAccount);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
        return accessToken;
    }

    private String generateImpersonatedCredentials(GoogleCredentials googleCredentials, String serviceAccount) throws IOException {
        String accessToken = googleCredentials.refreshAccessToken().getTokenValue();
        if (googleCredentials instanceof ImpersonatedCredentials) {
            ImpersonatedCredentials targetCredentials = ImpersonatedCredentials.create(
                googleCredentials,
                serviceAccount,
                null,
                null,
                1800
            );
            accessToken = targetCredentials.refreshAccessToken().getTokenValue();
        } else {
            log.error("ADC is not a service account. Unable to authenticate.");
        }
        return accessToken;
    }

}
