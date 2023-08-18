package org.squidmin.java.spring.gradle.bigquery.dto.bigquery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BigQuerySchemaItem {

    private String name;
    private String type;
    private String mode;

}
