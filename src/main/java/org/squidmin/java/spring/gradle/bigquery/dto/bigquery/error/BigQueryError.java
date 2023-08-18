package org.squidmin.java.spring.gradle.bigquery.dto.bigquery.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BigQueryError {

    private Error error;

}
