package org.squidmin.java.spring.gradle.bigquery.fixture;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.squidmin.java.spring.gradle.bigquery.TestUtil;
import org.squidmin.java.spring.gradle.bigquery.config.DataTypes;
import org.squidmin.java.spring.gradle.bigquery.config.Exclusions;
import org.squidmin.java.spring.gradle.bigquery.config.Field;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SchemaDefault;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SelectFieldsDefault;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.WhereFieldsDefault;
import org.squidmin.java.spring.gradle.bigquery.dao.RecordExample;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleRequest;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponse;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponseItem;
import org.squidmin.java.spring.gradle.bigquery.logger.Logger;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class BigQueryFunctionalTestFixture {

    public static final String RESOURCES_BASE_PATH = "src/test/resources/";

    private static final long CURRENT_DATE_TIME = 1691606220L;
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final List<String> fields = List.of(
        "id",
        "creation_timestamp",
        "last_update_timestamp",
        "column_a",
        "column_b"
    );

    public enum CLI_ARG_KEYS {
        GCP_ACCESS_TOKEN,
        GCP_PROJECT_ID, BQ_DATASET, BQ_TABLE,
        SCHEMA
    }

    @Value("${spring.cloud.gcp.project-id}")
    private String gcpProjectId;

    public static final Supplier<List<RecordExample>> DEFAULT_ROWS = () -> IntStream.range(0, 5)
        .mapToObj(i -> {
            LocalDateTime now = LocalDateTime.now(TimeZone.getDefault().toZoneId());
            String creationTimestamp = LocalDateTime.of(
                2023, Month.JANUARY, 1,
                now.getHour(), now.getMinute(), now.getSecond()
            ).minusDays(3).format(DateTimeFormatter.ISO_DATE_TIME);
            String lastUpdateTimestamp = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(now.toEpochSecond(ZoneOffset.UTC)),
                TimeZone.getDefault().toZoneId()
            ).format(DateTimeFormatter.ISO_DATE_TIME);
            return RecordExample.builder()
                .id(UUID.randomUUID().toString())
                .creationTimestamp(creationTimestamp)
                .lastUpdateTimestamp(lastUpdateTimestamp)
                .columnA("asdf1")
                .columnB("asdf2")
                .build();
        }).collect(Collectors.toList());

    public static String validQueryString(String projectId, String dataset, String table) {
        return String.format("SELECT * FROM `%s.%s.%s` LIMIT 10", projectId, dataset, table);
    }

    public static ExampleRequest validExampleRequest() throws IOException {
        ExampleRequest request = mapper.readValue(
            TestUtil.readJson("/requests/valid_request.json"),
            ExampleRequest.class
        );
        Logger.log(String.format("Request: %s", request), Logger.LogType.INFO);
        return request;
    }

    public static ExampleResponse validExampleResponse() {
        return ExampleResponse.builder()
            .rows(
                Collections.singletonList(
                    ExampleResponseItem.builder()
                        .id("1234")
                        .creationTimestamp(LocalDateTime.now().toString())
                        .lastUpdateTimestamp(LocalDateTime.now().toString())
                        .columnA("test")
                        .columnB("test")
                        .build()
                )
            )
            .build();
    }

    public static ExampleResponse validExampleResponseItems() throws IOException {
        return mapper.readValue(
            TestUtil.readJson("/responses/valid_example_response_1.json"),
            new TypeReference<>() {
            }
        );
    }

    public static String validBigQueryRestServiceResponse() throws IOException {
        return TestUtil.readJson("/responses/valid_bq_api_response.json");
    }

    public static SchemaDefault validSchemaDefault() throws IOException {
        List<Field> fields = mapper.readValue(
            TestUtil.readJson("/schema/schema.json"),
            new TypeReference<>() {
            }
        );
        SchemaDefault schema = new SchemaDefault();
        schema.getFields().addAll(fields);
        return schema;
    }

    public static DataTypes validDataTypes() {
        DataTypes dataTypes = new DataTypes();
        dataTypes.getDataTypes().addAll(List.of("STRING", "DATETIME", "BOOL"));
        return dataTypes;
    }

    public static SelectFieldsDefault validSelectFieldsDefault() {
        SelectFieldsDefault selectFields = new SelectFieldsDefault();
        selectFields.getFields().addAll(fields);
        return selectFields;
    }

    public static WhereFieldsDefault validWhereFieldsDefault() {
        WhereFieldsDefault whereFields = new WhereFieldsDefault();
        IntStream.range(0, fields.size()).forEach(i -> {
            Field field = new Field();
            field.setName(fields.get(i));
            whereFields.getFilters().add(field);
        });
        return whereFields;
    }

    public static Exclusions validExclusions() {
        Exclusions exclusions = new Exclusions();
        exclusions.getFields().add("excludedField");
        return exclusions;
    }

    public static HttpEntity<String> validHttpEntity(String gcpProjectId, String gcpDataset, String gcpTable) {
        return new HttpEntity<>(
            BigQueryFunctionalTestFixture.validQueryString(gcpProjectId, gcpDataset, gcpTable),
            BigQueryFunctionalTestFixture.validHttpHeaders()
        );
    }

    public static HttpHeaders validHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer ".concat("dummy_token"));
        return httpHeaders;
    }

}
