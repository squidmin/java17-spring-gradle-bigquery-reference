package org.squidmin.java.spring.gradle.bigquery.config;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SchemaDefault;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SelectFieldsDefault;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.WhereFieldsDefault;
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryServiceFactory;

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
    private final String gcpSaAccessToken = System.getProperty("GCP_SA_ACCESS_TOKEN");

    private final String gcpSaKeyPath;

    private final String gcpDefaultProjectId;
    private final String gcpDefaultDataset;
    private final String gcpDefaultTable;

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

    private BigQuery bigQuery;

    @Autowired
    public BigQueryConfig(@Value("${spring.cloud.gcp.config.credentials.location}") String gcpSaKeyPath,
                          @Value("${bigquery.application-default.project-id}") String gcpDefaultProjectId,
                          @Value("${bigquery.application-default.dataset}") String gcpDefaultDataset,
                          @Value("${bigquery.application-default.table}") String gcpDefaultTable,
                          @Value("${bigquery.service-account.project-id}") String gcpSaProjectId,
                          @Value("${bigquery.service-account.dataset}") String gcpSaDataset,
                          @Value("${bigquery.service-account.table}") String gcpSaTable,
                          @Value("${bigquery.uri.queries}") String queryUri,
                          SchemaDefault schemaDefault,
                          DataTypes dataTypes,
                          SelectFieldsDefault selectFieldsDefault,
                          WhereFieldsDefault whereFieldsDefault,
                          Exclusions exclusions,
                          @Value("${bigquery.select-all}") boolean selectAll) {

        this.gcpSaKeyPath = gcpSaKeyPath;

        this.gcpDefaultProjectId = gcpDefaultProjectId;
        this.gcpDefaultDataset = gcpDefaultDataset;
        this.gcpDefaultTable = gcpDefaultTable;

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

        this.bigQuery = BigQueryServiceFactory.defaultInstance(
            gcpSaKeyPath,
            gcpAccessToken,
            gcpSaAccessToken,
            gcpDefaultProjectId
        );

    }

    public void refreshGcpCredentials(String gcpToken) {
        BigQueryOptions.Builder bqOptionsBuilder = BigQueryOptions.newBuilder();
        bqOptionsBuilder.setProjectId(gcpDefaultProjectId).setLocation("us");
        this.bigQuery = bqOptionsBuilder.setCredentials(
            GoogleCredentials.newBuilder()
                .setAccessToken(AccessToken.newBuilder().setTokenValue(gcpToken).build())
                .build()
        ).build().getService();
    }

    @Bean
    @Primary
    public GoogleCredentials googleCredentials() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials.getApplicationDefault()
            .createScoped("https://www.googleapis.com/auth/cloud-platform");
        googleCredentials.refreshIfExpired();
        return googleCredentials;
    }

}
