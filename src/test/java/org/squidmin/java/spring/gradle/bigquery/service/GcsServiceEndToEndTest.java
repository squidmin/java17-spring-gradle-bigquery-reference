package org.squidmin.java.spring.gradle.bigquery.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.squidmin.java.spring.gradle.bigquery.CliConfig;
import org.squidmin.java.spring.gradle.bigquery.util.LoggerUtil;

public class GcsServiceEndToEndTest extends CliConfig {

    @Autowired
    private GcsService gcsService;

    @BeforeEach
    void beforeEach() {
        initialize();
        LoggerUtil.logRunConfig();
    }

}
