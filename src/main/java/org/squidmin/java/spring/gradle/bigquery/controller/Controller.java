package org.squidmin.java.spring.gradle.bigquery.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleRequest;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponse;
import org.squidmin.java.spring.gradle.bigquery.repository.ExampleRepositoryImpl;

import java.io.IOException;

@RestController
@RequestMapping("/bigquery")
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class Controller {

    private final ExampleRepositoryImpl exampleRepository;

    public Controller(ExampleRepositoryImpl exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    @PostMapping(
        value = "/query",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ExampleResponse> query(
        @RequestHeader("bq-api-token") String bqApiToken,
        @Valid @RequestBody ExampleRequest request) throws IOException {

        return exampleRepository.query(request, bqApiToken);

    }

}
