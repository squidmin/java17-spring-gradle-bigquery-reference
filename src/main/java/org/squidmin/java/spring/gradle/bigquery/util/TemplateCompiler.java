package org.squidmin.java.spring.gradle.bigquery.util;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.config.Field;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SchemaDefault;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleRequest;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleRequestItem;
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryTimeUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TemplateCompiler {

    private final BigQueryTimeUtil bigQueryTimeUtil;

    private final Handlebars handlebars;

    public TemplateCompiler(BigQueryTimeUtil bigQueryTimeUtil) {
        this.bigQueryTimeUtil = bigQueryTimeUtil;
        TemplateLoader loader = new ClassPathTemplateLoader("/templates/", ".hbs");
        handlebars = new Handlebars(loader);
        handlebars.registerHelpers(new HelperSource());
    }

    public void populatePresentValues(BigQueryConfig bigQueryConfig,
                                      List<String> presentValues,
                                      List<ExampleRequestItem> requestBody) {

        // Populate presentValues (e.g., names of fields having at least one value present)
        for (Field whereField : bigQueryConfig.getWhereFieldsDefault().getFilters()) {
            for (ExampleRequestItem requestItem : requestBody) {
                String fieldName = whereField.getName();
                String value = requestItem.get(fieldName);
                if (StringUtils.isNotEmpty(value) && !presentValues.contains(fieldName)) {
                    presentValues.add(fieldName);
                }
            }
        }

    }

    public String compile(
        String templateName,
        ExampleRequest request,
        BigQueryConfig bigQueryConfig) throws IOException {

        Template template = handlebars.compile(templateName);
        Map<String, Object> templateInput = new HashMap<>();
        List<String> presentValues = new ArrayList<>();

        List<ExampleRequestItem> requestBody = request.getSubqueries();

        populatePresentValues(bigQueryConfig, presentValues, requestBody);

        // Populate filtered request data (e.g., drop nulls from ExampleRequestItem)
        List<Map<String, String>> filteredRequestBody = new ArrayList<>();
        for (int i = 0; i < requestBody.size(); i++) {
            filteredRequestBody.add(new HashMap<>());
        }

        for (int i = 0; i < requestBody.size(); i++) {
            ExampleRequestItem requestItem = requestBody.get(i);
            for (String fieldName : presentValues) {
                String value = requestItem.get(fieldName);
                if (StringUtils.isNotEmpty(value) || isTimestampField(fieldName)) {
                    filteredRequestBody.get(i).put(fieldName, value);
                }
            }
        }

        SchemaDefault schema = bigQueryConfig.getSchemaDefault();
        List<String> columns = schema.getFields().stream()
            .map(Field::getName)
            .filter(name -> !bigQueryConfig.getExclusions().getFields().contains(name))
            .collect(Collectors.toList());

        templateInput.put("projectId", bigQueryConfig.getGcpDefaultProjectId());
        templateInput.put("dataset", bigQueryConfig.getGcpDefaultDataset());
        templateInput.put("table", bigQueryConfig.getGcpDefaultTable());
        templateInput.put("requestItems", filteredRequestBody);
        templateInput.put("whereFields", bigQueryConfig.getWhereFieldsDefault().getFilters());
        templateInput.put("presentValues", presentValues);
        templateInput.put("columns", columns);

        templateInput.put("currentDateTime", bigQueryTimeUtil.getCurrentDateTime());
        templateInput.put("currentDateTimeStart", bigQueryTimeUtil.getStartOfCurrentDateTime());
        templateInput.put("currentDateTimeEnd", bigQueryTimeUtil.getEndOfCurrentDateTime());

        return template.apply(templateInput);

    }

    private boolean isTimestampField(String fieldName) {
        return List.of("creationTimestamp", "lastUpdateTimestamp").contains(fieldName);
    }

}
