package org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "tables.default.select")
@RefreshScope
@Getter
public class SelectFieldsDefault {

    private final List<String> fields = new ArrayList<>();

}
