package org.squidmin.java.spring.gradle.bigquery.repository;

import org.springframework.http.ResponseEntity;
import org.squidmin.java.spring.gradle.bigquery.dao.RecordExample;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleRequest;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponse;
import org.squidmin.java.spring.gradle.bigquery.dto.bigquery.Query;

import java.io.IOException;
import java.util.List;

public interface ExampleRepository {

    ResponseEntity<ExampleResponse> query(Query query, String bqApiToken) throws IOException;

    ResponseEntity<ExampleResponse> query(ExampleRequest request, String bqApiToken) throws IOException;

    int insert(String projectId, String dataset, String table, List<RecordExample> records);

}
