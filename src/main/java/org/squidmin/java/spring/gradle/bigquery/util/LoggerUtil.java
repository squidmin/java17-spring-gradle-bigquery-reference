package org.squidmin.java.spring.gradle.bigquery.util;

import com.google.api.gax.paging.Page;
import com.google.cloud.bigquery.Dataset;
import com.google.cloud.bigquery.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.squidmin.java.spring.gradle.bigquery.config.DataTypes;
import org.squidmin.java.spring.gradle.bigquery.logger.Logger;

@Slf4j
public class LoggerUtil {

    public enum ProfileOption {DEFAULT, OVERRIDDEN, ACTIVE}

    public static void logBqProperties(RunEnvironment runEnvironment, ProfileOption profileOption) {
        Logger.echoHorizontalLine(Logger.LogType.INFO);
        if (profileOption == ProfileOption.DEFAULT) {
            Logger.log("--- BigQuery default properties ---", Logger.LogType.CYAN);
            logBqProperties(
                runEnvironment.getGcpDefaultUserProjectIdDefault(),
                runEnvironment.getGcpDefaultUserDatasetDefault(),
                runEnvironment.getGcpDefaultUserTableDefault(),
                runEnvironment.getGcpSaProjectIdDefault(),
                runEnvironment.getGcpSaDatasetDefault(),
                runEnvironment.getGcpSaTableDefault()
            );
        } else if (profileOption == ProfileOption.OVERRIDDEN) {
            Logger.log("--- BigQuery overridden properties ---", Logger.LogType.CYAN);
            logBqProperties(
                runEnvironment.getGcpDefaultUserProjectIdOverride(),
                runEnvironment.getGcpDefaultUserDatasetOverride(),
                runEnvironment.getGcpDefaultUserTableOverride(),
                runEnvironment.getGcpSaProjectIdOverride(),
                runEnvironment.getGcpSaDatasetOverride(),
                runEnvironment.getGcpSaTableOverride()
            );
        } else if (profileOption == ProfileOption.ACTIVE) {
            Logger.log("BigQuery resource properties currently configured:", Logger.LogType.CYAN);
            logBqProperties(
                runEnvironment.getGcpDefaultUserProjectId(),
                runEnvironment.getGcpDefaultUserDataset(),
                runEnvironment.getGcpDefaultUserTable(),
                runEnvironment.getGcpSaProjectId(),
                runEnvironment.getGcpSaDataset(),
                runEnvironment.getGcpSaTable()
            );
        } else {
            log.error("Error: IntegrationTest.echoBigQueryResourceMetadata(): Invalid option specified.");
        }
        Logger.echoHorizontalLine(Logger.LogType.INFO);
    }

    public static void logBqProperties(
        String gcpDefaultUserProjectId, String gcpDefaultUserDataset, String gcpDefaultUserTable,
        String gcpSaProjectId, String gcpSaDataset, String gcpSaTable) {

        Logger.log(String.format("Default Project ID: %s", gcpDefaultUserProjectId), Logger.LogType.INFO);
        Logger.log(String.format("Default Dataset name: %s", gcpDefaultUserDataset), Logger.LogType.INFO);
        Logger.log(String.format("Default Table name: %s", gcpDefaultUserTable), Logger.LogType.INFO);

        Logger.log(String.format("Service account Project ID: %s", gcpSaProjectId), Logger.LogType.INFO);
        Logger.log(String.format("Service account Dataset name: %s", gcpSaDataset), Logger.LogType.INFO);
        Logger.log(String.format("Service account Table name: %s", gcpSaTable), Logger.LogType.INFO);
    }

    public static void logRunConfig() {
        Logger.echoHorizontalLine(Logger.LogType.CYAN);
        Logger.log("Run config:", Logger.LogType.CYAN);
        Logger.echoHorizontalLine(Logger.LogType.CYAN);
        Logger.log(String.format("PROFILE                         %s", System.getProperty("PROFILE")), Logger.LogType.CYAN);
        Logger.log(String.format("GCP_SA_KEY_PATH                 %s", System.getProperty("GCP_SA_KEY_PATH")), Logger.LogType.CYAN);
        String adcAccessToken = System.getProperty("GCP_ADC_ACCESS_TOKEN");
        if (StringUtils.isNotEmpty(adcAccessToken)) {
            Logger.log(String.format("GCP_ADC_ACCESS_TOKEN            %s", adcAccessToken.substring(0, 9).concat("...")), Logger.LogType.CYAN);
        } else {
            Logger.log(String.format("GCP_ADC_ACCESS_TOKEN            %s", "Not specified"), Logger.LogType.CYAN);
        }
        String gcpSaAccessToken = System.getProperty("GCP_SA_ACCESS_TOKEN");
        if (StringUtils.isNotEmpty(gcpSaAccessToken)) {
            Logger.log(String.format("GCP_SA_ACCESS_TOKEN             %s", gcpSaAccessToken).substring(0, 9).concat("..."), Logger.LogType.CYAN);
        } else {
            Logger.log(String.format("GCP_SA_ACCESS_TOKEN             %s", "Not specified"), Logger.LogType.CYAN);
        }
        Logger.log(String.format("GCP_DEFAULT_USER_PROJECT_ID     %s", System.getProperty("GCP_DEFAULT_USER_PROJECT_ID")), Logger.LogType.CYAN);
        Logger.log(String.format("GCP_DEFAULT_USER_DATASET        %s", System.getProperty("GCP_DEFAULT_USER_DATASET")), Logger.LogType.CYAN);
        Logger.log(String.format("GCP_DEFAULT_USER_TABLE          %s", System.getProperty("GCP_DEFAULT_USER_TABLE")), Logger.LogType.CYAN);
        Logger.log(String.format("GCP_SA_PROJECT_ID               %s", System.getProperty("GCP_SA_PROJECT_ID")), Logger.LogType.CYAN);
        Logger.log(String.format("GCP_SA_DATASET                  %s", System.getProperty("GCP_SA_DATASET")), Logger.LogType.CYAN);
        Logger.log(String.format("GCP_SA_TABLE                    %s", System.getProperty("GCP_SA_TABLE")), Logger.LogType.CYAN);
        Logger.echoHorizontalLine(Logger.LogType.CYAN);
    }

    public static void logDatasets(String projectId, Page<Dataset> datasets) {
        Logger.echoHorizontalLine(Logger.LogType.INFO);
        Logger.log(String.format("Project \"%s\" datasets:", projectId), Logger.LogType.INFO);
        Logger.echoHorizontalLine(Logger.LogType.INFO);
        datasets.iterateAll().forEach(
            dataset -> Logger.log(
                String.format("Dataset ID: %s", dataset.getDatasetId()),
                Logger.LogType.INFO
            )
        );
        Logger.echoHorizontalLine(Logger.LogType.INFO);
    }

    public static void logCreateTable(TableInfo tableInfo) {
        Logger.echoHorizontalLine(Logger.LogType.INFO);
        Logger.log(
            String.format("Creating table \"%s\". Find the table information below:", tableInfo.getTableId()),
            Logger.LogType.INFO
        );
        logTableInfo(tableInfo);
        Logger.echoHorizontalLine(Logger.LogType.INFO);
    }

    public static void logDataTypes(DataTypes dataTypes) {
        Logger.log("Supported data types: ", Logger.LogType.CYAN);
        dataTypes.getDataTypes().forEach(type -> Logger.log(type, Logger.LogType.CYAN));
    }

    public static void logTableInfo(TableInfo tableInfo) {
        log.info("Friendly name: " + tableInfo.getFriendlyName());
        log.info("Description: " + tableInfo.getDescription());
        log.info("Creation time: " + tableInfo.getCreationTime());
        log.info("Expiration time: " + tableInfo.getExpirationTime());
    }

    public static void logSaKeyFileAuth(boolean isBqJdkAuthenticatedUsingSaKeyFile) {
        if (isBqJdkAuthenticatedUsingSaKeyFile) {
            Logger.log("BigQuery Java SDK has authenticated successfully using a service account key file.", Logger.LogType.INFO);
        } else {
            Logger.log("BigQuery JDK was not able to authenticate using a service account key file.", Logger.LogType.INFO);
            Logger.log("Attempting to authenticate using Application Default Credentials.", Logger.LogType.INFO);
        }
    }

}
