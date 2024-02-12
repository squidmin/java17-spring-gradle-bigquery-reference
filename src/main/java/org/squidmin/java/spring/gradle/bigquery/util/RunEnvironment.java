package org.squidmin.java.spring.gradle.bigquery.util;

import com.google.cloud.bigquery.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RunEnvironment {

    // Default profile values
    private String gcpProjectIdDefault;
    private String gcpDatasetDefault;
    private String gcpTableDefault;
    //

    // Overridden values
    private String gcpProjectIdOverride;
    private String gcpDatasetOverride;
    private String gcpTableOverride;
    //

    // Active profile values
    private String gcpProjectId;
    private String gcpDataset;
    private String gcpTable;
    //

    private Schema schema;

}
