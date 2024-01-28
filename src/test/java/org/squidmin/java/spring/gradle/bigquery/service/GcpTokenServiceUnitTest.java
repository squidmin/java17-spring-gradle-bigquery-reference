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
public class GcpTokenServiceUnitTest {

    @Autowired
    private String gcpDefaultProjectId;

    @Autowired
    private RestTemplate restTemplateMock;

    private GcpTokenService gcpTokenService;

    @BeforeEach
    void beforeEach() throws IOException {
        gcpTokenService = new GcpTokenService(gcpDefaultProjectId, restTemplateMock);
    }

    @Test
    void generateIdentityToken_return200OkResponseWithIdentityToken() {
        String IDENTITY_TOKEN = "identity_token";
        Mockito.when(
            restTemplateMock.exchange(
                ArgumentMatchers.any(RequestEntity.class),
                ArgumentMatchers.eq(String.class)
            )
        ).thenReturn(new ResponseEntity<>(IDENTITY_TOKEN, HttpStatus.OK));
        Assertions.assertEquals(IDENTITY_TOKEN, gcpTokenService.generateIdentityToken());
    }

    @Test
    void generateAccessToken_return200OkResponseWithAccessToken() {
        Assertions.assertTrue(gcpTokenService.generateAccessToken().contains("ya29"));
    }

}
