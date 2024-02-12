package org.squidmin.java.spring.gradle.bigquery;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.Schema;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.config.GcsConfig;
import org.squidmin.java.spring.gradle.bigquery.config.IntegrationTestConfig;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SchemaDefault;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;
import org.squidmin.java.spring.gradle.bigquery.service.BigQueryService;
import org.squidmin.java.spring.gradle.bigquery.service.GcsService;
import org.squidmin.java.spring.gradle.bigquery.util.RunEnvironment;
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryUtil;

@SpringBootTest(classes = {BigQueryService.class, IntegrationTestConfig.class})
@ActiveProfiles("integration")
@Slf4j
public abstract class CliConfig {

    @Autowired
    protected BigQueryConfig bigQueryConfig;

    @Autowired
    protected BigQuery bigQuery;

    @Autowired
    protected GcsConfig gcsConfig;

    @Autowired
    protected GcsService gcsService;

    protected String gcpProjectIdDefault;
    protected String gcpDatasetDefault;
    protected String gcpTableDefault;
    protected String gcpProjectIdCliOverride;
    protected String gcpDatasetCliOverride;
    protected String gcpTableCliOverride;
    protected String schemaOverrideString;

    protected SchemaDefault schemaDefault;
    protected Schema _schemaOverride;

    // The default values of configured BigQuery resource properties can be overridden by the values of CLI arguments.
    protected String GCP_ACCESS_TOKEN;
    protected String GCP_PROJECT_ID;
    protected String BQ_DATASET;
    protected String BQ_TABLE;
    protected Schema SCHEMA;

    protected RunEnvironment runEnvironment = RunEnvironment.builder().build();

    protected void initialize() {
        initRunEnvironmentDefaultValues();
        initRunEnvironmentOverriddenValues();
        initRunEnvironment();
        initRunEnvironmentActiveProperties();
    }

    protected void initRunEnvironmentDefaultValues() {
        // Class-level initializers.
        gcpProjectIdDefault = bigQueryConfig.getGcpProjectId();
        gcpDatasetDefault = bigQueryConfig.getGcpDataset();
        gcpTableDefault = bigQueryConfig.getGcpTable();

        schemaDefault = bigQueryConfig.getSchemaDefault();

        // Set default run environment properties from Spring @Configuration classes.
        runEnvironment = RunEnvironment.builder()
            .gcpProjectIdDefault(bigQueryConfig.getGcpProjectId())
            .gcpDatasetDefault(bigQueryConfig.getGcpDataset())
            .gcpTableDefault(bigQueryConfig.getGcpTable())
            .schema(BigQueryUtil.InlineSchemaTranslator.translate(bigQueryConfig.getSchemaDefault(), bigQueryConfig.getDataTypes()))
            .build();
    }

    private void initRunEnvironmentOverriddenValues() {
        gcpProjectIdCliOverride = System.getProperty(BigQueryFunctionalTestFixture.CLI_ARG_KEYS.GCP_PROJECT_ID.name());
        gcpDatasetCliOverride = System.getProperty(BigQueryFunctionalTestFixture.CLI_ARG_KEYS.BQ_DATASET.name());
        gcpTableCliOverride = System.getProperty(BigQueryFunctionalTestFixture.CLI_ARG_KEYS.BQ_TABLE.name());

        schemaOverrideString = System.getProperty(BigQueryFunctionalTestFixture.CLI_ARG_KEYS.SCHEMA.name());
        if (StringUtils.isNotEmpty(schemaOverrideString)) {
            _schemaOverride = BigQueryUtil.InlineSchemaTranslator.translate(schemaOverrideString, bigQueryConfig.getDataTypes());
        }
    }

    private void initRunEnvironment() {
        // Run environment defaults.
        runEnvironment.setGcpProjectIdDefault(gcpProjectIdDefault);
        runEnvironment.setGcpDatasetDefault(gcpDatasetDefault);
        runEnvironment.setGcpTableDefault(gcpTableDefault);

        // Override default properties with values of CLI arguments.
        runEnvironment.setGcpProjectId(setEnvProperty(gcpProjectIdDefault, gcpProjectIdCliOverride));
        runEnvironment.setGcpDataset(setEnvProperty(gcpDatasetDefault, gcpDatasetCliOverride));
        runEnvironment.setGcpTable(setEnvProperty(gcpTableDefault, gcpTableCliOverride));

        // Set table schema in the run environment.
        runEnvironment.setSchema(
            StringUtils.isNotEmpty(schemaOverrideString) ?
                _schemaOverride :
                BigQueryUtil.InlineSchemaTranslator.translate(schemaDefault, bigQueryConfig.getDataTypes())
        );
    }

    private void initRunEnvironmentActiveProperties() {
        // Set integration test class level variables for active run environment.
        GCP_ACCESS_TOKEN = bigQueryConfig.getGcpAccessToken();
        GCP_PROJECT_ID = setEnvProperty(bigQueryConfig.getGcpProjectId(), runEnvironment.getGcpProjectId());
        BQ_DATASET = setEnvProperty(bigQueryConfig.getGcpDataset(), runEnvironment.getGcpDataset());
        BQ_TABLE = setEnvProperty(bigQueryConfig.getGcpTable(), runEnvironment.getGcpTable());
        SCHEMA = runEnvironment.getSchema();
    }

    private String setEnvProperty(String defaultValue, String overrideValue) {
        return StringUtils.isNotEmpty(overrideValue) ? overrideValue : defaultValue;
    }

}
