package org.squidmin.java.spring.gradle.bigquery.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "bigquery")
@RefreshScope
@Getter
@Slf4j
public class DataTypes {

    private final List<String> dataTypes = new ArrayList<>();

}
