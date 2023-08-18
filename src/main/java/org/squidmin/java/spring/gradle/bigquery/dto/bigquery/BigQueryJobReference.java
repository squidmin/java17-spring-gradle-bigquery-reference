package org.squidmin.java.spring.gradle.bigquery.dto.bigquery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BigQueryJobReference {

    private String projectId;
    private String jobId;
    private String location;

}
