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

@ActiveProfiles({"integration"})
@SpringBootTest(classes = {UnitTestConfig.class})
@Slf4j
public class GcpTokenServiceUnitTest {

    private static final String TOKEN = "test_token";

    @Autowired
    private String gcpProjectId;

    @Autowired
    private RestTemplate restTemplateMock;

    private GcpTokenService gcpTokenService;

    @BeforeEach
    void beforeEach() {
        gcpTokenService = new GcpTokenService(gcpProjectId, restTemplateMock);
        Mockito.when(
            restTemplateMock.exchange(
                ArgumentMatchers.any(RequestEntity.class),
                ArgumentMatchers.eq(String.class)
            )
        ).thenReturn(new ResponseEntity<>(TOKEN, HttpStatus.OK));
    }

    @Test
    void generateIdentityToken_return200OkResponseWithIdentityToken() {
        Assertions.assertEquals(TOKEN, gcpTokenService.generateIdentityToken());
    }

    @Test
    void generateAccessToken_return200OkResponseWithAccessToken() {
        String actual = gcpTokenService.generateAccessToken();
        Assertions.assertEquals("", actual);
    }

}
