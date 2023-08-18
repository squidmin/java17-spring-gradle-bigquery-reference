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
public class BigQueryRow {

    private List<BigQueryRowValue> f;

}
