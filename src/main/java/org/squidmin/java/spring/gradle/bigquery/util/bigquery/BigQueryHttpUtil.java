package org.squidmin.java.spring.gradle.bigquery.util.bigquery;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponse;
import org.squidmin.java.spring.gradle.bigquery.dto.bigquery.BigQueryRestServiceResponse;
import org.squidmin.java.spring.gradle.bigquery.dto.bigquery.error.BigQueryError;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Component
@Slf4j
public class BigQueryHttpUtil {

    private final RestTemplate restTemplate;

    public BigQueryHttpUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<ExampleResponse> callBigQueryApi(
        BigQueryConfig bigQueryConfig,
        HttpEntity<String> request,
        ObjectMapper mapper) throws IOException {

        long requestTime = System.currentTimeMillis();
        String queryUri = bigQueryConfig.getQueryUri();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(queryUri, request, String.class);
        log.info(
            "callBigQueryApi(): Received response after {} seconds",
            String.format("%.5f", (System.currentTimeMillis() - requestTime) / 1000f)
        );
        String responseBody = responseEntity.getBody();
        if (HttpStatus.OK != responseEntity.getStatusCode()) {
            return handleHttpStatusNotOk(responseEntity, responseBody, mapper);
        }
        BigQueryRestServiceResponse response = mapper.readValue(responseBody, BigQueryRestServiceResponse.class);
        if (isOkBigQueryApiResponse(responseBody, response)) {
            boolean selectAll = bigQueryConfig.isSelectAll();
            return buildOkExampleResponse(bigQueryConfig, responseBody, selectAll);
        } else {
            log.error("Response body is empty");
            return new ResponseEntity<>(ExampleResponse.builder().rows(new ArrayList<>()).build(), HttpStatus.ACCEPTED);
        }

    }

    public static ExampleResponse initExampleResponse() {
        ExampleResponse response = new ExampleResponse();
        response.setRows(new ArrayList<>());
        return response;
    }

    public static boolean isOkExampleResponse(String responseBody, BigQueryRestServiceResponse response) {
        return null != responseBody && null != response.getRows() && !response.getRows().isEmpty();
    }

    public static boolean isOkExampleResponse(ResponseEntity<ExampleResponse> responseEntity) {
        return HttpStatus.OK == responseEntity.getStatusCode() && null != responseEntity.getBody();
    }

    public static boolean isOkBigQueryApiResponse(String responseBody, BigQueryRestServiceResponse response) {
        return null != responseBody && null != response.getRows() && !response.getRows().isEmpty();
    }

    private static ResponseEntity<ExampleResponse> handleHttpStatusNotOk(
        ResponseEntity<String> bqApiResponseEntity,
        String responseBody,
        ObjectMapper mapper) throws JsonProcessingException {

        ResponseEntity<ExampleResponse> exampleResponseEntity = new ResponseEntity<>(HttpStatus.OK);
        if (HttpStatus.OK != bqApiResponseEntity.getStatusCode()) {
            BigQueryError bqError = mapper.readValue(responseBody, BigQueryError.class);
            ExampleResponse response = ExampleResponse.builder().errors(new ArrayList<>()).build();
            bqError.getError().getErrors().forEach(err -> response.getErrors().add(err.toString()));
            exampleResponseEntity = new ResponseEntity<>(response, bqApiResponseEntity.getStatusCode());
        }
        return exampleResponseEntity;

    }

    private static ResponseEntity<ExampleResponse> buildOkExampleResponse(
        BigQueryConfig bigQueryConfig,
        String responseBody,
        boolean selectAll) throws IOException {

        return new ResponseEntity<>(
            ExampleResponse.builder()
                .rows(
                    BigQueryUtil.toList(
                        responseBody.getBytes(StandardCharsets.UTF_8),
                        bigQueryConfig.getSelectFieldsDefault(),
                        selectAll
                    )
                )
                .build(),
            HttpStatus.OK
        );

    }

}
