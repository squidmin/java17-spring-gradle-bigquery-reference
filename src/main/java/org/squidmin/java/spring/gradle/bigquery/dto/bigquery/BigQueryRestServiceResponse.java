package org.squidmin.java.spring.gradle.bigquery.dto.bigquery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BigQueryRestServiceResponse {

    private String kind;
    private BigQuerySchema schema;
    private BigQueryJobReference jobReference;
    private String totalRows;
    private List<BigQueryRow> rows;
    private String totalBytesProcessed;
    private boolean jobComplete;
    private boolean cacheHit;

}
