package org.squidmin.java.spring.gradle.bigquery.service;

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
        ).thenReturn(new ResponseEntity<>("asdf-1234", HttpStatus.OK));
        String expected = "asdf-1234";
        String actual = gcpTokenGeneratorService.generateIdentityToken();
        Assertions.assertEquals(expected, actual);
    }

}
