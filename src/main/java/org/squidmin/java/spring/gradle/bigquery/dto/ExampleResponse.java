package org.squidmin.java.spring.gradle.bigquery.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ExampleResponse {

    private List<ExampleResponseItem> body;

    private String url;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;

    public static String getCsvHeaders() {
        List<String> fieldNames = Arrays.stream(ExampleResponseItem.class.getDeclaredFields())
            .map(Field::getName)
            .toList();
        return String.join(",", fieldNames);
    }

}
