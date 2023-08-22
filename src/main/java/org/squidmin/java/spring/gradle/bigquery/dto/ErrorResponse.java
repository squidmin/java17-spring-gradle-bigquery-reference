package org.squidmin.java.spring.gradle.bigquery.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ErrorResponse {
    private String errorMessage;
    private Map<String, String> errors; // field name -> error message

    public ErrorResponse(String errorMessage, Map<String, String> errors) {
        this.errorMessage = errorMessage;
        this.errors = errors;
    }
}
