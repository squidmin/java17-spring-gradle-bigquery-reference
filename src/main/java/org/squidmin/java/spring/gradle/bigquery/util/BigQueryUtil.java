package org.squidmin.java.spring.gradle.bigquery.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.bigquery.Field;
import com.google.cloud.bigquery.Schema;
import com.google.cloud.bigquery.StandardSQLTypeName;
import com.google.cloud.bigquery.TableResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.config.DataTypes;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SchemaDefault;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SelectFieldsDefault;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleRequest;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponseItem;
import org.squidmin.java.spring.gradle.bigquery.dto.bigquery.BigQueryRestServiceResponse;
import org.squidmin.java.spring.gradle.bigquery.dto.bigquery.BigQueryRow;
import org.squidmin.java.spring.gradle.bigquery.dto.bigquery.BigQueryRowValue;
import org.squidmin.java.spring.gradle.bigquery.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BigQueryUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    private final TemplateCompiler templateCompiler;

    public BigQueryUtil(TemplateCompiler templateCompiler) {
        this.templateCompiler = templateCompiler;
    }

    public String buildQueryString(
        String templateName,
        ExampleRequest request,
        BigQueryConfig bigQueryConfig) throws IOException {

        String s = trimWhitespace(templateCompiler.compile(templateName, request, bigQueryConfig));
        return s;

    }

    public static String trimWhitespace(String str) {
        return str
            .replaceAll("\n", " ")
            .replaceAll("\\p{Zs}+", " ");
    }

    public static List<ExampleResponseItem> toList(TableResult tableResult) {
        List<ExampleResponseItem> response = new ArrayList<>();
        if (null != tableResult && 0 < tableResult.getTotalRows()) {
            tableResult.iterateAll().forEach(
                row -> response.add(
                    ExampleResponseItem.builder()
                        .id(row.get(0).getStringValue())
                        .creationTimestamp(row.get(1).getStringValue())
                        .lastUpdateTimestamp(row.get(2).getStringValue())
                        .columnA(row.get(3).getStringValue())
                        .columnB(row.get(4).getStringValue())
                        .build()
                )
            );
        }
        return response;
    }

    public static List<ExampleResponseItem> toList(
        byte[] tableResult,
        SelectFieldsDefault selectFieldsDefault,
        boolean isSelectAll) throws IOException {

        List<ExampleResponseItem> response = new ArrayList<>();
        BigQueryRestServiceResponse bqResponse = mapper.readValue(tableResult, BigQueryRestServiceResponse.class);
        List<BigQueryRow> rows = bqResponse.getRows();
        if (null != rows) {
            for (BigQueryRow r : rows) {
                ExampleResponseItem exampleResponseItem = new ExampleResponseItem();
                List<BigQueryRowValue> f = r.getF();
                if (!isSelectAll) {
                    for (int j = 0; j < selectFieldsDefault.getFields().size(); j++) {
                        String name = selectFieldsDefault.getFields().get(j);
                        setResponseItem(exampleResponseItem, f, j, name);
                    }
                    response.add(exampleResponseItem);
                } else {
                    for (int j = 0; j < bqResponse.getSchema().getFields().size(); j++) {
                        String name = bqResponse.getSchema().getFields().get(j).getName();
                        setResponseItem(exampleResponseItem, f, j, name);
                    }
                    response.add(exampleResponseItem);
                }
            }
        }
        return response;

    }

    private static void setResponseItem(
        ExampleResponseItem exampleResponseItem,
        List<BigQueryRowValue> f,
        int index,
        String name) {

        BigQueryRowValue v = f.get(index);
        String value = v.getV();
        exampleResponseItem.setFromBigQueryResponse(name, value);

    }

    private static void addResponseItems(
        ExampleResponseItem responseItem,
        List<BigQueryRowValue> f,
        BigQueryConfig bigQueryConfig,
        List<String> excludeFields) {

        List<String> selectFields = bigQueryConfig.getSelectFieldsDefault().getFields();
        for (int j = 0; j < selectFields.size(); j++) {
            String name = selectFields.get(j);
            if (!excludeFields.contains(name)) {
                setResponseItem(responseItem, f, j, name);
            }
        }

    }

    public static class InlineSchemaTranslator {
        public static Schema translate(SchemaDefault schemaDefault, DataTypes dataTypes) {
            Logger.log(String.format("Generating Schema object using: \"%s\"...", schemaDefault.getFields()), Logger.LogType.CYAN);
            List<Field> fields = new ArrayList<>();
            schemaDefault.getFields().forEach(
                f -> {
                    log.info("name={}, type={}", f.getName(), f.getType());
                    fields.add(
                        Field.of(
                            f.getName(),
                            translateType(f.getType(), dataTypes)
                        )
                    );
                }
            );
            Logger.log("Finished generating schema.", Logger.LogType.CYAN);
            return Schema.of(fields);
        }

        public static Schema translate(String schema, DataTypes dataTypes) {
            Logger.log(String.format("Generating Schema object using CLI arg: \"%s\"...", schema), Logger.LogType.CYAN);
            List<Field> fields = new ArrayList<>();
            List<String> _fields = Arrays.stream(schema.split(",")).collect(Collectors.toList());
            _fields.forEach(
                f -> {
                    String[] split = f.split(":");
                    String name = split[0], type = split[1];
                    log.info("name={}, type={}", name, type);
                    fields.add(
                        Field.of(
                            name,
                            translateType(type, dataTypes)
                        )
                    );
                }
            );
            Logger.log("Finished generating schema.", Logger.LogType.CYAN);
            return Schema.of(fields);
        }

        private static StandardSQLTypeName translateType(String type, DataTypes dataTypes) {
            if (dataTypes.getDataTypes().contains(type)) {
                return StandardSQLTypeName.valueOf(type);
            } else {
                Logger.log(
                    "Error: BigQueryConfig.translateType(): Data type not supported. Defaulting to 'StandardSQLTypeNames.STRING'.",
                    Logger.LogType.ERROR
                );
                LoggerUtil.logDataTypes(dataTypes);
                return StandardSQLTypeName.STRING;
            }
        }
    }

}
