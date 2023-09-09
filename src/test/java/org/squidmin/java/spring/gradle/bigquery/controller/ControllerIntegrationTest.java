package org.squidmin.java.spring.gradle.bigquery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.squidmin.java.spring.gradle.bigquery.TestUtil;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.config.ControllerIntegrationTestConfig;
import org.squidmin.java.spring.gradle.bigquery.config.GcsConfig;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleRequest;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponse;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;

@Disabled
@ActiveProfiles("integration")
@ContextConfiguration(classes = ControllerIntegrationTestConfig.class)
@WebMvcTest(Controller.class)
@Slf4j
public class ControllerIntegrationTest {

    private final String QUERY_ENDPOINT = "/bigquery/query";

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Autowired
    private BigQueryConfig bigQueryConfig;

    @Autowired
    private GcsConfig gcsConfig;

    @Autowired
    private RestTemplate restTemplateMock;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void contextLoads() {
        Assertions.assertNotNull(bigQueryConfig.getGcpSaKeyPath());
        Assertions.assertNotNull(gcsConfig.getGcpSaKeyPath());
    }

    @Test
    void query_givenValidRequest_thenReturnHttpStatusOk_andResponseBodyIsNotNull() throws Exception {
        apiReturnsResponse(BigQueryFunctionalTestFixture.validBigQueryRestServiceResponse(), HttpStatus.OK);
        ExampleRequest request = mapper.readValue(
            TestUtil.readJson("/requests/multiple_request_items.json"),
            ExampleRequest.class
        );
        MockHttpServletResponse mockHttpServletResponse = callApi(QUERY_ENDPOINT, request);
        Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());
        final ExampleResponse response = mapper.readValue(
            mockHttpServletResponse.getContentAsString(),
            ExampleResponse.class
        );
        Assertions.assertNotNull(response);
    }

    private MockHttpServletResponse callApi(String endpoint, ExampleRequest request) throws Exception {
        String content = mapper.writeValueAsString(request);
        return mockMvc.perform(
            MockMvcRequestBuilders
                .post(endpoint)
                .header("gcp-token", "")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content)
        ).andReturn().getResponse();
    }

    private void apiReturnsResponse(String body, HttpStatus httpStatus) {
        Mockito.when(
            restTemplateMock.postForEntity(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpEntity.class),
                ArgumentMatchers.eq(String.class)
            )
        ).thenReturn(new ResponseEntity<>(body, httpStatus));
    }

}
