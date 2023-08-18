package org.squidmin.java.spring.gradle.bigquery.util;

public class StringUtils {

    public static boolean isEmpty(String str) { return null == str || 0 == str.length(); }

    public static boolean isNotEmpty(String str) { return null != str && 0 != str.length(); }

}
