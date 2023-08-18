package org.squidmin.java.spring.gradle.bigquery.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "tables.classifications.select.exclusions")
@RefreshScope
@Getter
@Setter
@Slf4j
public class Exclusions {

    private final List<String> fields = new ArrayList<>();

}
