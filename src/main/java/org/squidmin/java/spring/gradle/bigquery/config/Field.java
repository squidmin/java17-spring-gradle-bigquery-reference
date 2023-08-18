package org.squidmin.java.spring.gradle.bigquery.config;

import lombok.Data;

@Data
public class Field {

    private String name;
    private String type;
    private String mode;
    private String description;
    private String defaultValueExpression;

}
