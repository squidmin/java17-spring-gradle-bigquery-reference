package org.squidmin.java.spring.gradle.bigquery.config;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SchemaDefault;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SelectFieldsDefault;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.WhereFieldsDefault;
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryInstanceProvider;

import java.io.IOException;

@Configuration
@ComponentScan(basePackages = {
    "org.squidmin.java.spring.gradle.bigquery"
})
@Getter
@Setter
@Slf4j
public class BigQueryConfig {

    private final String systemArgGcpSaKeyPath = System.getProperty("GCP_SA_KEY_PATH");
    private final String gcpAccessToken = System.getProperty("GCP_ACCESS_TOKEN");

    private final String gcpSaKeyPath;

    private final String gcpProjectId;
    private final String gcpDataset;
    private final String gcpTable;

    private final String queryUri;

    private final SchemaDefault schemaDefault;
    private final DataTypes dataTypes;
    private final SelectFieldsDefault selectFieldsDefault;
    private final WhereFieldsDefault whereFieldsDefault;
    private final Exclusions exclusions;

    private final boolean selectAll;

    private BigQuery bigQuery;

    @Autowired
    public BigQueryConfig(
        @Value("${spring.cloud.gcp.config.credentials.location}") String gcpSaKeyPath,
        @Value("${bigquery.application-default.project-id}") String gcpProjectId,
        @Value("${bigquery.application-default.dataset}") String gcpDataset,
        @Value("${bigquery.application-default.table}") String gcpTable,
        @Value("${bigquery.uri.queries}") String queryUri,
        SchemaDefault schemaDefault,
        DataTypes dataTypes,
        SelectFieldsDefault selectFieldsDefault,
        WhereFieldsDefault whereFieldsDefault,
        Exclusions exclusions,
        @Value("${bigquery.select-all}") boolean selectAll) {

        this.gcpSaKeyPath = StringUtils.isEmpty(systemArgGcpSaKeyPath) ? gcpSaKeyPath : systemArgGcpSaKeyPath;

        this.gcpProjectId = gcpProjectId;
        this.gcpDataset = gcpDataset;
        this.gcpTable = gcpTable;

        this.queryUri = queryUri;

        this.schemaDefault = schemaDefault;
        this.dataTypes = dataTypes;
        this.selectFieldsDefault = selectFieldsDefault;
        this.whereFieldsDefault = whereFieldsDefault;
        this.exclusions = exclusions;

        this.selectAll = selectAll;

        this.bigQuery = BigQueryInstanceProvider.defaultInstance(
            this.gcpSaKeyPath,
            gcpAccessToken,
            gcpProjectId
        );

    }

    public void setGcpCredentials(String gcpToken) throws IOException {
        BigQueryOptions.Builder bqOptionsBuilder = BigQueryOptions.newBuilder();
        bqOptionsBuilder.setProjectId(gcpProjectId).setLocation("us");
        GoogleCredentials credentials = GoogleCredentials.newBuilder()
            .setAccessToken(AccessToken.newBuilder().setTokenValue(gcpToken).build())
            .build();
        credentials.refreshIfExpired();
        this.bigQuery = bqOptionsBuilder.setCredentials(credentials).build().getService();
    }

}
