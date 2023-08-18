package org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.squidmin.java.spring.gradle.bigquery.config.Field;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "tables.default.schema")
@RefreshScope
@Getter
public class SchemaDefault {

    private final List<Field> fields = new ArrayList<>();

}
