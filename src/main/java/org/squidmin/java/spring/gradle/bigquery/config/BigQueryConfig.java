package org.squidmin.java.spring.gradle.bigquery.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SchemaDefault;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SelectFieldsDefault;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.WhereFieldsDefault;

@Configuration
@ComponentScan(basePackages = {
    "org.squidmin.java.spring.gradle.bigquery"
})
@Getter
@Setter
@Slf4j
public class BigQueryConfig {

    private final String gcpDefaultUserProjectId;
    private final String gcpDefaultUserDataset;
    private final String gcpDefaultUserTable;

    private final String gcpSaProjectId;
    private final String gcpSaDataset;
    private final String gcpSaTable;

    private final String queryUri;

    private final SchemaDefault schemaDefault;
    private final DataTypes dataTypes;
    private final SelectFieldsDefault selectFieldsDefault;
    private final WhereFieldsDefault whereFieldsDefault;
    private final Exclusions exclusions;

    private final boolean selectAll;

    private final BigQueryOptionsConfig bigQueryOptionsConfig;

    @Autowired
    public BigQueryConfig(@Value("${bigquery.application-default.project-id}") String gcpDefaultUserProjectId,
                          @Value("${bigquery.application-default.dataset}") String gcpDefaultUserDataset,
                          @Value("${bigquery.application-default.table}") String gcpDefaultUserTable,
                          @Value("${bigquery.service-account.project-id}") String gcpSaProjectId,
                          @Value("${bigquery.service-account.dataset}") String gcpSaDataset,
                          @Value("${bigquery.service-account.table}") String gcpSaTable,
                          @Value("${bigquery.uri.queries}") String queryUri,
                          SchemaDefault schemaDefault,
                          DataTypes dataTypes,
                          SelectFieldsDefault selectFieldsDefault,
                          WhereFieldsDefault whereFieldsDefault,
                          Exclusions exclusions,
                          @Value("${bigquery.select-all}") boolean selectAll,
                          BigQueryOptionsConfig bigQueryOptionsConfig) {

        this.gcpDefaultUserProjectId = gcpDefaultUserProjectId;
        this.gcpDefaultUserDataset = gcpDefaultUserDataset;
        this.gcpDefaultUserTable = gcpDefaultUserTable;

        this.gcpSaProjectId = gcpSaProjectId;
        this.gcpSaDataset = gcpSaDataset;
        this.gcpSaTable = gcpSaTable;

        this.queryUri = queryUri;

        this.schemaDefault = schemaDefault;
        this.dataTypes = dataTypes;
        this.selectFieldsDefault = selectFieldsDefault;
        this.whereFieldsDefault = whereFieldsDefault;
        this.exclusions = exclusions;

        this.selectAll = selectAll;

        this.bigQueryOptionsConfig = bigQueryOptionsConfig;

    }

}