package org.squidmin.java.spring.gradle.bigquery.util;

import com.google.cloud.bigquery.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RunEnvironment {

    // Default profile values
    private String gcpDefaultUserProjectIdDefault;
    private String gcpDefaultUserDatasetDefault;
    private String gcpDefaultUserTableDefault;

    private String gcpSaProjectIdDefault;
    private String gcpSaDatasetDefault;
    private String gcpSaTableDefault;
    //

    // Overridden values
    private String gcpDefaultUserProjectIdOverride;
    private String gcpDefaultUserDatasetOverride;
    private String gcpDefaultUserTableOverride;

    private String gcpSaProjectIdOverride;
    private String gcpSaDatasetOverride;
    private String gcpSaTableOverride;
    //

    // Active profile values
    private String gcpDefaultUserProjectId;
    private String gcpDefaultUserDataset;
    private String gcpDefaultUserTable;

    private String gcpSaProjectId;
    private String gcpSaDataset;
    private String gcpSaTable;
    //

    private Schema schema;

}