package org.squidmin.java.spring.gradle.bigquery.util;

public class StringUtil {

    public static String trimWhitespace(String str) {
        return str
            .replaceAll("\n", " ")
            .replaceAll("\\p{Zs}+", " ");
    }

}
