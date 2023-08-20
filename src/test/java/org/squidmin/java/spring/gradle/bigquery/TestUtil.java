package org.squidmin.java.spring.gradle.bigquery;

import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtil {

    public static String readJson(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(BigQueryFunctionalTestFixture.RESOURCES_BASE_PATH.concat(path))));
    }

    public static String readQueryString(String filename) throws IOException {
        return BigQueryUtil.trimWhitespace(
            new String(
                Files.readAllBytes(
                    Paths.get(
                        BigQueryFunctionalTestFixture.RESOURCES_BASE_PATH.concat("queries/").concat(filename)
                    )
                )
            )
        );
    }

    public static String trimWhitespace(String str) {
        return str
            .replaceAll("\n", " ")
            .replaceAll("\\p{Zs}+", " ");
    }

}
