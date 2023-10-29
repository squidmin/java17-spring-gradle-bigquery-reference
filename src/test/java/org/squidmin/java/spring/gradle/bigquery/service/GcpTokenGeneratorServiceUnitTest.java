package org.squidmin.java.spring.gradle.bigquery.service;

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

    @Autowired
    private String gcpDefaultUserProjectId;

    @Autowired
    private RestTemplate restTemplateMock;

    private GcpTokenGeneratorService gcpTokenGeneratorService;

    @BeforeEach
    void beforeEach() throws IOException {
        gcpTokenGeneratorService = new GcpTokenGeneratorService(gcpDefaultUserProjectId, restTemplateMock);
    }

    @Test
    void generateIdentityToken_return200OkResponseWithIdentityToken() {
        String IDENTITY_TOKEN = "identity-token_1234";

        Mockito.when(
            restTemplateMock.exchange(
                ArgumentMatchers.any(RequestEntity.class),
                ArgumentMatchers.eq(String.class)
            )
        ).thenReturn(new ResponseEntity<>(IDENTITY_TOKEN, HttpStatus.OK));
        Assertions.assertEquals(IDENTITY_TOKEN, gcpTokenGeneratorService.generateIdentityToken());
    }

    @Test
    void generateAccessToken_return200OkResponseWithAccessToken() {
        Assertions.assertTrue(gcpTokenGeneratorService.generateAccessToken().contains("ya29"));
    }

}
