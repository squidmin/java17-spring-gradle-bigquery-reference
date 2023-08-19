package org.squidmin.java.spring.gradle.bigquery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.squidmin.java.spring.gradle.bigquery.logger.Logger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExampleRequestItem {

    private String id;
    private String creationTimestamp;
    private String lastUpdateTimestamp;
    private String columnA;
    private String columnB;

//    @FieldAConstraint
//    private String fieldA;

    public String get(String fieldName) {
        if ("id".equalsIgnoreCase(fieldName)) {
            return id;
        } else if ("creation_timestamp".equalsIgnoreCase(fieldName)) {
            return creationTimestamp;
        } else if ("last_update_timestamp".equalsIgnoreCase(fieldName)) {
            return lastUpdateTimestamp;
        } else if ("column_a".equalsIgnoreCase(fieldName)) {
            return columnA;
        } else if ("column_b".equalsIgnoreCase(fieldName)) {
            return columnB;
        } else {
            Logger.log(String.format("Invalid field name: %s", fieldName), Logger.LogType.ERROR);
        }
        return null;
    }

}
