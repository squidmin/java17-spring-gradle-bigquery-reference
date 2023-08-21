package org.squidmin.java.spring.gradle.bigquery.repository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.squidmin.java.spring.gradle.bigquery.dao.RecordExample;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleRequest;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponse;
import org.squidmin.java.spring.gradle.bigquery.dto.Query;
import org.squidmin.java.spring.gradle.bigquery.service.BigQueryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Getter
@Slf4j
public class ExampleRepositoryImpl implements ExampleRepository {

    private final BigQueryService bigQueryService;

    public ExampleRepositoryImpl(BigQueryService bigQueryService) {
        this.bigQueryService = bigQueryService;
    }

    @Override
    public ResponseEntity<ExampleResponse> query(Query query, String bqApiToken) throws IOException {
        return bigQueryService.query(query, bqApiToken);
    }

    @Override
    public ResponseEntity<ExampleResponse> query(ExampleRequest request, String bqApiToken) throws IOException {
        if (request.getBody().isEmpty()) {
            return new ResponseEntity<>(
                ExampleResponse.builder().body(new ArrayList<>()).build(),
                HttpStatus.OK
            );
        }
        return bigQueryService.query(request, bqApiToken);
    }

    @Override
    public int insert(String projectId, String dataset, String table, List<RecordExample> records) {
        int numRowsInserted = bigQueryService.insert(projectId, dataset, table, records).size();
        return 0 < numRowsInserted ? numRowsInserted : -1;
    }

}
