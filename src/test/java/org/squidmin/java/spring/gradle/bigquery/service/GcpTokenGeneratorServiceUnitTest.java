package org.squidmin.java.spring.gradle.bigquery.service;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.squidmin.java.spring.gradle.bigquery.config.UnitTestConfig;

import java.io.IOException;

@ActiveProfiles({"integration"})
@SpringBootTest(classes = {UnitTestConfig.class})
@Slf4j
public class GcpTokenGeneratorServiceUnitTest {

    private static final String ACCESS_TOKEN = "access-token_1234";
    private static final String IDENTITY_TOKEN = "identity-token_1234";

    @Autowired
    private String gcpDefaultUserProjectId;

    @Autowired
    private GoogleCredentials googleCredentialsMock;

    @Autowired
    private RestTemplate restTemplateMock;

    private GcpTokenGeneratorService gcpTokenGeneratorService;

    @BeforeEach
    void beforeEach() throws IOException {
        gcpTokenGeneratorService = new GcpTokenGeneratorService(
            gcpDefaultUserProjectId,
            googleCredentialsMock,
            restTemplateMock
        );
    }

    @Test
    void generateIdentityToken_return200OkResponseWithIdentityToken() {
        Mockito.when(
            restTemplateMock.exchange(
                ArgumentMatchers.any(RequestEntity.class),
                ArgumentMatchers.eq(String.class)
            )
        ).thenReturn(new ResponseEntity<>(IDENTITY_TOKEN, HttpStatus.OK));
        String actual = gcpTokenGeneratorService.generateIdentityToken();
        Assertions.assertEquals(IDENTITY_TOKEN, actual);
    }

    @Test
    void generateAccessToken_return200OkResponseWithAccessToken() throws IOException {
        Mockito.when(googleCredentialsMock.refreshAccessToken())
            .thenReturn(AccessToken.newBuilder().setTokenValue(ACCESS_TOKEN).build());
        String actual = gcpTokenGeneratorService.generateAccessToken();
        Assertions.assertEquals(ACCESS_TOKEN, actual);
    }

}
