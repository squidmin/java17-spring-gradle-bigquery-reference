package org.squidmin.java.spring.gradle.bigquery.util;

import com.github.jknack.handlebars.Options;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class HelperSource {

    public CharSequence eq(Object context, Options options) throws IOException {
        Object obj = options.param(0);
        return Objects.equals(context, obj) ? options.fn() : options.inverse();
    }

    public String getRequestItemValue(Object context, Options options) {
        Integer index = options.get("index");
        String fieldName = options.get("this.name");
        List<Map<String, String>> requestItems = options.get("requestItems");
        String value = requestItems.get(index).get(fieldName);
        return StringUtils.isEmpty(value) ? null : value;
    }

    public boolean isEmpty(String context, Options options) {
        return StringUtils.isEmpty(context);
    }

    public boolean isNotEmpty(String context, Options options) {
        return StringUtils.isNotEmpty(context);
    }

    public int increment(Object context, Options options) {
        return ((Integer) context) + 1;
    }

    public int sizeof(Object context, Options options) {
        if (context instanceof List) {
            return ((List<?>) context).size();
        }
        return -1;
    }

}
