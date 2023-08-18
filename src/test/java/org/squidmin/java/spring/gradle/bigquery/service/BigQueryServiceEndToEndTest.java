package org.squidmin.java.spring.gradle.bigquery.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.squidmin.java.spring.gradle.bigquery.CliConfig;
import org.squidmin.java.spring.gradle.bigquery.util.LoggerUtil;

@Slf4j
public class BigQueryServiceEndToEndTest extends CliConfig {

    @BeforeEach
    void beforeEach() {
        initialize();
        LoggerUtil.logRunConfig();
    }

    @Test
    public void echoRunConfig() {
        LoggerUtil.logBqProperties(runEnvironment, LoggerUtil.ProfileOption.DEFAULT);
    }

}
