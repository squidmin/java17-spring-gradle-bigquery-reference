package org.squidmin.java.spring.gradle.bigquery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.squidmin.java.spring.gradle.bigquery.TestUtil;
import org.squidmin.java.spring.gradle.bigquery.config.ControllerIntegrationTestConfig;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleRequest;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponse;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;
import org.squidmin.java.spring.gradle.bigquery.service.GcsService;
import org.squidmin.java.spring.gradle.bigquery.util.Constants;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

//@Disabled
@ActiveProfiles("integration")
@ContextConfiguration(classes = ControllerIntegrationTestConfig.class)
@WebMvcTest(BigQueryController.class)
@Slf4j
public class BigQueryControllerIntegrationTest {

    private final String QUERY_ENDPOINT = "/bigquery/query";

    @Autowired
    private String gcpSaKeyPath;

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Autowired
    private GcsService gcsServiceMock;

    @Autowired
    private RestTemplate restTemplateMock;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void contextLoads() {
        Assertions.assertNotNull(gcpSaKeyPath);
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
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders
                .post(endpoint)
                .header(Constants.HttpHeaders.GCP_ACCESS_TOKEN, "access_token")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request))
        ).andReturn();
        return result.getResponse();
    }

    private void apiReturnsResponse(String body, HttpStatus httpStatus) throws MalformedURLException {
        Mockito.when(
            restTemplateMock.postForEntity(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpEntity.class),
                ArgumentMatchers.eq(String.class)
            )
        ).thenReturn(new ResponseEntity<>(body, httpStatus));
        Mockito.when(gcsServiceMock.upload(ArgumentMatchers.any(List.class)))
            .thenReturn(new URL("http", "localhost", "8080"));
    }

}
