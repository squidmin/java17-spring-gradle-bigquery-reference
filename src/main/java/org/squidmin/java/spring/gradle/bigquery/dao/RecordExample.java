package org.squidmin.java.spring.gradle.bigquery.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.squidmin.java.spring.gradle.bigquery.logger.Logger;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordExample {

    private String id;
    private String creationTimestamp;
    private String lastUpdateTimestamp;
    private String columnA;
    private String columnB;

    private Map<String, String> fields = new HashMap<>();

    public String getField(String field) {
        if (field.equalsIgnoreCase("id")) {
            return id;
        } else if (field.equalsIgnoreCase("creation_timestamp")) {
            return creationTimestamp;
        } else if (field.equalsIgnoreCase("last_update_timestamp")) {
            return lastUpdateTimestamp;
        } else if (field.equalsIgnoreCase("column_a")) {
            return columnA;
        } else if (field.equalsIgnoreCase("column_b")) {
            return columnB;
        } else {
            Logger.log(String.format("Invalid field: %s", field), Logger.LogType.ERROR);
        }
        return Strings.EMPTY;
    }

}
