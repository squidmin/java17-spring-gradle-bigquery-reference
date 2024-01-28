package org.squidmin.java.spring.gradle.bigquery;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryUtil;
import org.squidmin.java.spring.gradle.bigquery.util.RunEnvironment;

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

    protected String gcpDefaultProjectIdDefault;
    protected String gcpDefaultDatasetDefault;
    protected String gcpDefaultTableDefault;
    protected String gcpDefaultProjectIdCliOverride;
    protected String gcpDefaultDatasetCliOverride;
    protected String gcpDefaultTableCliOverride;
    protected String gcpSaProjectIdDefault;
    protected String gcpSaDatasetDefault;
    protected String gcpSaTableDefault;
    protected String gcpSaProjectIdCliOverride;
    protected String gcpSaDatasetCliOverride;
    protected String gcpSaTableCliOverride;
    protected String schemaOverrideString;

    protected SchemaDefault schemaDefault;
    protected Schema _schemaOverride;

    // The default values of configured BigQuery resource properties can be overridden by the values of CLI arguments.
    protected String GCP_ACCESS_TOKEN;
    protected String GCP_DEFAULT_PROJECT_ID;
    protected String GCP_DEFAULT_DATASET;
    protected String GCP_DEFAULT_TABLE;
    protected String GCP_SA_PROJECT_ID;
    protected String GCP_SA_DATASET;
    protected String GCP_SA_TABLE;
    protected Schema SCHEMA;

    protected RunEnvironment runEnvironment = RunEnvironment.builder().build();

    protected final ObjectMapper mapper = new ObjectMapper();

    protected void initialize() {
        initRunEnvironmentDefaultValues();
        initRunEnvironmentOverriddenValues();
        initRunEnvironment();
        initRunEnvironmentActiveProperties();
    }

    protected void initRunEnvironmentDefaultValues() {
        // Class-level initializers.
        gcpDefaultProjectIdDefault = bigQueryConfig.getGcpDefaultProjectId();
        gcpDefaultDatasetDefault = bigQueryConfig.getGcpDefaultDataset();
        gcpDefaultTableDefault = bigQueryConfig.getGcpDefaultTable();

        gcpSaProjectIdDefault = bigQueryConfig.getGcpSaProjectId();
        gcpSaDatasetDefault = bigQueryConfig.getGcpSaDataset();
        gcpSaTableDefault = bigQueryConfig.getGcpSaTable();

        schemaDefault = bigQueryConfig.getSchemaDefault();

        // Set default run environment properties from Spring @Configuration classes.
        runEnvironment = RunEnvironment.builder()
            .gcpDefaultProjectIdDefault(bigQueryConfig.getGcpDefaultProjectId())
            .gcpDefaultDatasetDefault(bigQueryConfig.getGcpDefaultDataset())
            .gcpDefaultTableDefault(bigQueryConfig.getGcpDefaultTable())
            .gcpSaProjectId(bigQueryConfig.getGcpSaProjectId())
            .gcpSaDataset(bigQueryConfig.getGcpSaDataset())
            .gcpSaTable(bigQueryConfig.getGcpSaTable())
            .schema(BigQueryUtil.InlineSchemaTranslator.translate(bigQueryConfig.getSchemaDefault(), bigQueryConfig.getDataTypes()))
            .build();
    }

    private void initRunEnvironmentOverriddenValues() {
        gcpDefaultProjectIdCliOverride = System.getProperty(BigQueryFunctionalTestFixture.CLI_ARG_KEYS.GCP_DEFAULT_PROJECT_ID.name());
        gcpDefaultDatasetCliOverride = System.getProperty(BigQueryFunctionalTestFixture.CLI_ARG_KEYS.GCP_DEFAULT_DATASET.name());
        gcpDefaultTableCliOverride = System.getProperty(BigQueryFunctionalTestFixture.CLI_ARG_KEYS.GCP_DEFAULT_TABLE.name());

        gcpSaProjectIdCliOverride = System.getProperty(BigQueryFunctionalTestFixture.CLI_ARG_KEYS.GCP_SA_PROJECT_ID.name());
        gcpSaDatasetCliOverride = System.getProperty(BigQueryFunctionalTestFixture.CLI_ARG_KEYS.GCP_SA_DATASET.name());
        gcpSaTableCliOverride = System.getProperty(BigQueryFunctionalTestFixture.CLI_ARG_KEYS.GCP_SA_TABLE.name());

        schemaOverrideString = System.getProperty(BigQueryFunctionalTestFixture.CLI_ARG_KEYS.SCHEMA.name());
        if (StringUtils.isNotEmpty(schemaOverrideString)) {
            _schemaOverride = BigQueryUtil.InlineSchemaTranslator.translate(schemaOverrideString, bigQueryConfig.getDataTypes());
        }
    }

    private void initRunEnvironment() {
        // Run environment defaults.
        runEnvironment.setGcpDefaultProjectIdDefault(gcpDefaultProjectIdDefault);
        runEnvironment.setGcpDefaultDatasetDefault(gcpDefaultDatasetDefault);
        runEnvironment.setGcpDefaultTableDefault(gcpDefaultTableDefault);

        runEnvironment.setGcpSaProjectIdDefault(gcpSaProjectIdDefault);
        runEnvironment.setGcpSaDatasetDefault(gcpSaDatasetDefault);
        runEnvironment.setGcpSaTableDefault(gcpSaTableDefault);

        // Override default properties with values of CLI arguments.
        runEnvironment.setGcpDefaultProjectId(setEnvProperty(gcpDefaultProjectIdDefault, gcpDefaultProjectIdCliOverride));
        runEnvironment.setGcpDefaultDataset(setEnvProperty(gcpDefaultDatasetDefault, gcpDefaultDatasetCliOverride));
        runEnvironment.setGcpDefaultTable(setEnvProperty(gcpDefaultTableDefault, gcpDefaultTableCliOverride));

        runEnvironment.setGcpSaProjectId(setEnvProperty(gcpSaProjectIdDefault, gcpSaProjectIdCliOverride));
        runEnvironment.setGcpSaDataset(setEnvProperty(gcpSaDatasetDefault, gcpSaDatasetCliOverride));
        runEnvironment.setGcpSaTable(setEnvProperty(gcpSaTableDefault, gcpSaTableCliOverride));

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
        GCP_DEFAULT_PROJECT_ID = setEnvProperty(bigQueryConfig.getGcpDefaultProjectId(), runEnvironment.getGcpDefaultProjectId());
        GCP_DEFAULT_DATASET = setEnvProperty(bigQueryConfig.getGcpDefaultDataset(), runEnvironment.getGcpDefaultDataset());
        GCP_DEFAULT_TABLE = setEnvProperty(bigQueryConfig.getGcpDefaultTable(), runEnvironment.getGcpDefaultTable());

        GCP_SA_PROJECT_ID = setEnvProperty(bigQueryConfig.getGcpSaProjectId(), runEnvironment.getGcpSaProjectId());
        GCP_SA_DATASET = setEnvProperty(bigQueryConfig.getGcpSaDataset(), runEnvironment.getGcpSaDataset());
        GCP_SA_TABLE = setEnvProperty(bigQueryConfig.getGcpSaTable(), runEnvironment.getGcpSaTable());

        SCHEMA = runEnvironment.getSchema();
    }

    private String setEnvProperty(String defaultValue, String overrideValue) {
        return StringUtils.isNotEmpty(overrideValue) ? overrideValue : defaultValue;
    }

}
