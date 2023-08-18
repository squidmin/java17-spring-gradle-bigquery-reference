package org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.squidmin.java.spring.gradle.bigquery.config.Field;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "tables.default.where")
@RefreshScope
@Getter
public class WhereFieldsDefault {

    private final List<Field> filters = new ArrayList<>();

}
