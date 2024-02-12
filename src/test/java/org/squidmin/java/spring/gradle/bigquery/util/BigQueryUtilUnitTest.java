package org.squidmin.java.spring.gradle.bigquery.util;

import com.google.cloud.bigquery.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.squidmin.java.spring.gradle.bigquery.UnitTest;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.config.UnitTestConfig;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleRequest;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponseItem;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryFunctionalTestFixture;
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ActiveProfiles({"integration"})
@ContextConfiguration(classes = {UnitTestConfig.class})
@Slf4j
public class BigQueryUtilUnitTest extends UnitTest {

    @Autowired
    @Qualifier("gcpProjectId_unitTest")
    private String gcpProjectId;

    @Autowired
    @Qualifier("gcpDataset_unitTest")
    private String gcpDataset;

    @Autowired
    @Qualifier("gcpTable_unitTest")
    private String gcpTable;

    @Autowired
    @Qualifier("queryUri_unitTest")
    private String queryUri;

    private final BigQueryConfig bigQueryConfigMock = Mockito.mock(BigQueryConfig.class);
    private final TemplateCompiler templateCompilerMock = Mockito.mock(TemplateCompiler.class);

    private BigQueryUtil bigQueryUtil;

    @BeforeEach
    public void beforeEach() {
        setUp(
            bigQueryConfigMock,
            gcpProjectId,
            gcpDataset,
            gcpTable,
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

    @Test
    void buildQueryString_returnQueryString() throws IOException {
        ExampleRequest request = new ExampleRequest();
        String expected = "select * from `a.b.c` limit 5";
        Mockito.when(templateCompilerMock.compile(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(expected);
        BigQueryUtil bigQueryUtil = new BigQueryUtil(templateCompilerMock);
        String actual = bigQueryUtil.buildQueryString("template", request, bigQueryConfigMock);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toList_withTableResult() {
        TableResult tableResult = Mockito.mock(TableResult.class);

        Mockito.when(tableResult.getTotalRows()).thenReturn(1L);

        FieldValueList row1 = FieldValueList.of(List.of(
                FieldValue.of(FieldValue.Attribute.PRIMITIVE, "asdf-1234"),
                FieldValue.of(FieldValue.Attribute.PRIMITIVE, "2013-06-24T00:00:00"),
                FieldValue.of(FieldValue.Attribute.PRIMITIVE, "2015-04-20T00:00:00"),
                FieldValue.of(FieldValue.Attribute.PRIMITIVE, "column_a_test"),
                FieldValue.of(FieldValue.Attribute.PRIMITIVE, "column_b_test")
            )
        );
        List<FieldValueList> rows = List.of(row1);
        Mockito.when(tableResult.iterateAll()).thenReturn(rows);

        List<ExampleResponseItem> response = bigQueryUtil.toList(tableResult);

        Assertions.assertEquals(1, response.size());
    }

}
