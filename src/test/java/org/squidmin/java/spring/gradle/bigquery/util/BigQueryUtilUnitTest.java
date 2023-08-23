package org.squidmin.java.spring.gradle.bigquery.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.squidmin.java.spring.gradle.bigquery.UnitTest;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.config.UnitTestConfig;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;

import java.io.IOException;
import java.util.ArrayList;

@ContextConfiguration(classes = {UnitTestConfig.class})
@Slf4j
public class BigQueryUtilUnitTest extends UnitTest {

    @Autowired
    private String gcpDefaultUserProjectId;
    @Autowired
    private String gcpDefaultUserDataset;
    @Autowired
    private String gcpDefaultUserTable;
    @Autowired
    private String queryUri;

    private final BigQueryConfig bigQueryConfigMock = Mockito.mock(BigQueryConfig.class);
    private final TemplateCompiler templateCompilerMock = Mockito.mock(TemplateCompiler.class);

    private BigQueryUtil bigQueryUtil;

    @BeforeEach
    public void beforeEach() {
        setUp(
            bigQueryConfigMock,
            gcpDefaultUserProjectId,
            gcpDefaultUserDataset,
            gcpDefaultUserTable,
            queryUri
        );
        bigQueryUtil = new BigQueryUtil(templateCompilerMock);
    }

    @Test
    void toList_givenByteArray_whenTheByteArrayIsEmpty_thenReturnEmptyList() throws IOException {
        Assertions.assertEquals(
            new ArrayList<>(),
            BigQueryUtil.toList(
                new byte[]{'{', '}'},
                BigQueryFunctionalTestFixture.validSelectFieldsDefault(),
                false
            )
        );
    }

}
