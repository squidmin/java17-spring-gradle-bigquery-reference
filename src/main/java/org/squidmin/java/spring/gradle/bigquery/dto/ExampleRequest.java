package org.squidmin.java.spring.gradle.bigquery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.squidmin.java.spring.gradle.bigquery.validation.ExampleRequestBodyConstraint;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExampleRequest {

    private String projectId;
    private String dataset;
    private String table;

    @ExampleRequestBodyConstraint
    private List<ExampleRequestItem> subqueries;

    private int limit;

}
