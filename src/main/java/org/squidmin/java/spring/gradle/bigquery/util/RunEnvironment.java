package org.squidmin.java.spring.gradle.bigquery.util;

import com.google.cloud.bigquery.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RunEnvironment {

    // Default profile values
    private String gcpDefaultProjectIdDefault;
    private String gcpDefaultDatasetDefault;
    private String gcpDefaultTableDefault;

    private String gcpSaProjectIdDefault;
    private String gcpSaDatasetDefault;
    private String gcpSaTableDefault;
    //

    // Overridden values
    private String gcpDefaultProjectIdOverride;
    private String gcpDefaultDatasetOverride;
    private String gcpDefaultTableOverride;

    private String gcpSaProjectIdOverride;
    private String gcpSaDatasetOverride;
    private String gcpSaTableOverride;
    //

    // Active profile values
    private String gcpDefaultProjectId;
    private String gcpDefaultDataset;
    private String gcpDefaultTable;

    private String gcpSaProjectId;
    private String gcpSaDataset;
    private String gcpSaTable;
    //

    private Schema schema;

}
