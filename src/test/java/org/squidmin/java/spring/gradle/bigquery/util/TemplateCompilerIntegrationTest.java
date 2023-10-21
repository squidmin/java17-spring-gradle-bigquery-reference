package org.squidmin.java.spring.gradle.bigquery.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.squidmin.java.spring.gradle.bigquery.TestUtil;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.config.IntegrationTestConfig;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryTimeUtil;

import java.io.IOException;

@SpringBootTest(classes = {IntegrationTestConfig.class})
@ActiveProfiles("integration")
public class TemplateCompilerIntegrationTest {

    @Autowired
    private BigQueryConfig bigQueryConfig;

    @Autowired
    private BigQueryTimeUtil bigQueryTimeUtil;

    private TemplateCompiler templateCompiler;

    @BeforeEach
    void beforeEach() {
        templateCompiler = new TemplateCompiler(bigQueryTimeUtil);
    }

    @Test
    void compile_givenValidExampleRequest_thenReturnValidQueryString() throws IOException {
        String expected = TestUtil.readQueryString("valid_query.sql");
        String actual = TestUtil.trimWhitespace(
            templateCompiler.compile(
                "query_1",
                BigQueryFunctionalTestFixture.validExampleRequest(),
                bigQueryConfig
            )
        );
        Assertions.assertEquals(expected, actual);
    }

}
