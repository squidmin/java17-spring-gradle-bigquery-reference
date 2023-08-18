package org.squidmin.java.spring.gradle.bigquery.dto.bigquery.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorItem {

    private String message;
    private String domain;
    private String reason;
    private String location;
    private String locationType;

}
