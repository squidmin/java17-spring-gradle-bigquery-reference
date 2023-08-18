package org.squidmin.java.spring.gradle.bigquery.dto.bigquery.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Error {

    private int code;
    private String message;
    private List<ErrorItem> errors;
    private String status;

}
