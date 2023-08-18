package org.squidmin.java.spring.gradle.bigquery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ExampleResponseItem {

    private String id;
    private String creationTimestamp;
    private String lastUpdateTimestamp;
    private String columnA;
    private String columnB;

    public void setFromBigQueryResponse(String name, String value) {
        if (name.equalsIgnoreCase("id")) {
            setId(value);
        } else if (name.equalsIgnoreCase("creation_timestamp")) {
            setCreationTimestamp(value);
        } else if (name.equalsIgnoreCase("last_update_timestamp")) {
            setLastUpdateTimestamp(value);
        } else if (name.equalsIgnoreCase("column_a")) {
            setColumnA(value);
        } else if (name.equalsIgnoreCase("column_b")) {
            setColumnB(value);
        } else {
            log.error("Value associated with name is null.");
        }
    }

}
