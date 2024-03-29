package org.squidmin.java.spring.gradle.bigquery.service;

import autovalue.shaded.com.google.common.collect.ImmutableMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.paging.Page;
import com.google.cloud.bigquery.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.config.DataTypes;
import org.squidmin.java.spring.gradle.bigquery.config.Exclusions;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SchemaDefault;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SelectFieldsDefault;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.WhereFieldsDefault;
import org.squidmin.java.spring.gradle.bigquery.dao.RecordExample;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleRequest;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleRequestItem;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponse;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponseItem;
import org.squidmin.java.spring.gradle.bigquery.dto.bigquery.Query;
import org.squidmin.java.spring.gradle.bigquery.exception.CustomJobException;
import org.squidmin.java.spring.gradle.bigquery.logger.Logger;
import org.squidmin.java.spring.gradle.bigquery.logger.LoggerUtil;
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryHttpUtil;
import org.squidmin.java.spring.gradle.bigquery.util.bigquery.BigQueryUtil;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@Service
@EnableConfigurationProperties(value = {
    SchemaDefault.class,
    SelectFieldsDefault.class,
    WhereFieldsDefault.class,
    DataTypes.class,
    Exclusions.class,
})
@Getter
@Slf4j
public class BigQueryService {

    private final String gcpProjectId;
    private final String gcpDataset;
    private final String gcpTable;

    private final boolean shouldUploadBqResponseToGcs = true;
    private final int responseSizeLimit;

    private final BigQueryUtil bigQueryUtil;

    private final BigQueryHttpUtil bigQueryHttpUtil;

    private final BigQuery bigQuery;

    private final BigQueryConfig bigQueryConfig;

    private final GcsService gcsService;

    private final ObjectMapper mapper;

    @Autowired
    public BigQueryService(@Value("${bigquery.response-size-limit}") int responseSizeLimit,
                           BigQueryUtil bigQueryUtil,
                           BigQueryHttpUtil bigQueryHttpUtil,
                           BigQueryConfig bigQueryConfig,
                           GcsService gcsService) {

        this.responseSizeLimit = responseSizeLimit;
        this.bigQueryConfig = bigQueryConfig;
        this.bigQuery = bigQueryConfig.getBigQuery();
        this.gcpProjectId = bigQueryConfig.getGcpProjectId();
        this.gcpDataset = bigQueryConfig.getGcpDataset();
        this.gcpTable = bigQueryConfig.getGcpTable();
        this.bigQueryUtil = bigQueryUtil;
        this.bigQueryHttpUtil = bigQueryHttpUtil;
        this.gcsService = gcsService;
        mapper = new ObjectMapper();

    }

    /**
     * <a href="https://cloud.google.com/bigquery/docs/listing-datasets#list_datasets">List datasets</a>
     */
    public List<String> listDatasets() {
        List<String> datasetsList = new ArrayList<>();
        try {
            Page<Dataset> datasets = bigQuery.listDatasets(gcpProjectId, BigQuery.DatasetListOption.pageSize(100));
            if (null == datasets) {
                Logger.log(
                    String.format("Dataset \"%s\" does not contain any models.", gcpDataset),
                    Logger.LogType.ERROR
                );
                return new ArrayList<>();
            }
            datasets.iterateAll().forEach(dataset -> datasetsList.add(dataset.getFriendlyName()));
            LoggerUtil.logDatasets(gcpProjectId, datasets);
        } catch (BigQueryException e) {
            Logger.log(
                String.format("Project \"%s\" does not contain any datasets.", gcpProjectId),
                Logger.LogType.ERROR
            );
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
        }
        return datasetsList;
    }

    /**
     * <a href="https://cloud.google.com/bigquery/docs/listing-datasets#get_information_about_datasets">Get information about datasets</a>
     */
    public void getDatasetInfo() {
        try {
            DatasetId datasetId = DatasetId.of(gcpProjectId, gcpDataset);
            Dataset dataset = bigQuery.getDataset(datasetId);

            // View dataset properties
            String description = dataset.getDescription();
            Logger.log(description, Logger.LogType.INFO);

            // View tables in the dataset
            // For more information on listing tables see:
            // https://javadoc.io/static/com.google.cloud/google-cloud-bigquery/0.22.0-beta/com/google/cloud/bigquery/BigQuery.html
            Page<Table> tables = bigQuery.listTables(gcpDataset, BigQuery.TableListOption.pageSize(100));

            tables.iterateAll().forEach(table -> Logger.log(table.getTableId().getTable() + "\n", Logger.LogType.INFO));

            Logger.log("Dataset info retrieved successfully.", Logger.LogType.INFO);
        } catch (BigQueryException e) {
            Logger.log("Dataset info not retrieved.\n" + e, Logger.LogType.ERROR);
        }
    }

    public boolean createDataset(String dataset) {
        try {
            DatasetInfo datasetInfo = DatasetInfo.newBuilder(dataset).build();
            Dataset newDataset = bigQuery.create(datasetInfo);
            String newDatasetName = newDataset.getDatasetId().getDataset();
            Logger.log(String.format("Dataset \"%s\" created successfully.", newDatasetName), Logger.LogType.INFO);
        } catch (BigQueryException e) {
            Logger.log(
                String.format("%s: Dataset \"%s\" was not created.", e.getClass().getName(), dataset),
                Logger.LogType.ERROR
            );
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
            return false;
        }
        return true;
    }

    public void deleteDataset(String projectId, String dataset) {
        try {
            DatasetId datasetId = DatasetId.of(projectId, dataset);
            boolean success = bigQuery.delete(datasetId);
            if (success) {
                Logger.log(String.format("Dataset \"%s\" deleted successfully.", dataset), Logger.LogType.INFO);
            } else {
                Logger.log(String.format("Dataset \"%s\" was not found in project \"%s\".", dataset, projectId), Logger.LogType.INFO);
            }
        } catch (BigQueryException e) {
            Logger.log(String.format("Dataset \"%s\" was not deleted.", dataset), Logger.LogType.ERROR);
        }
    }

    public void deleteDatasetAndContents(String projectId, String dataset) {
        try {
            DatasetId datasetId = DatasetId.of(projectId, dataset);
            boolean success = bigQuery.delete(datasetId, BigQuery.DatasetDeleteOption.deleteContents());
            if (success) {
                Logger.log(String.format("Dataset \"%s\" and its contents deleted successfully.", dataset), Logger.LogType.INFO);
            } else {
                Logger.log(String.format("Dataset \"%s\" was not found in project \"%s\".", dataset, projectId), Logger.LogType.INFO);
            }
        } catch (BigQueryException e) {
            Logger.log(String.format("Dataset \"%s\" was not deleted with contents.", dataset), Logger.LogType.ERROR);
        }
    }

    public boolean createTable(String dataset, String table) {
        Schema schema = BigQueryUtil.InlineSchemaTranslator.translate(bigQueryConfig.getSchemaDefault(), bigQueryConfig.getDataTypes());
        return createTable(dataset, table, schema);
    }

    public boolean createTable(String dataset, String table, Schema schema) {
        try {
            TableId tableId = TableId.of(dataset, table);
            TableDefinition tableDefinition = StandardTableDefinition.of(schema);
            TableInfo tableInfo = TableInfo.newBuilder(tableId, tableDefinition).build();
            LoggerUtil.logCreateTable(tableInfo);
            bigQuery.create(tableInfo);
            Logger.log(String.format("Table \"%s\" created successfully.", table), Logger.LogType.INFO);
        } catch (BigQueryException e) {
            Logger.log(
                String.format("%s: Table \"%s\" was not created.", e.getClass().getName(), table),
                Logger.LogType.ERROR
            );
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
            return false;
        }
        return true;
    }

    public void deleteTable(String projectId, String dataset, String table) {
        try {
            boolean success = bigQuery.delete(TableId.of(projectId, dataset, table));
            if (success) {
                Logger.log("Table deleted successfully", Logger.LogType.INFO);
            } else {
                Logger.log("Table was not found", Logger.LogType.INFO);
            }
        } catch (BigQueryException e) {
            Logger.log(String.format("Table %s was not deleted.", table), Logger.LogType.ERROR);
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
        }
    }

    public List<InsertAllRequest.RowToInsert> insert(String projectId, String dataset, String table, List<RecordExample> records) {
        try {
            List<InsertAllRequest.RowToInsert> rowsToInsert = new ArrayList<>();
            TableId tableId = TableId.of(projectId, dataset, table);
            Map<String, Object> rowContent = new HashMap<>();
            records.forEach(record -> {
                bigQueryConfig.getSchemaDefault().getFields().forEach(field -> {
                    String fieldName = field.getName();
                    rowContent.put(fieldName, record.getField(fieldName));
                });
                rowsToInsert.add(InsertAllRequest.RowToInsert.of(UUID.randomUUID().toString(), rowContent));
            });
            InsertAllResponse response = bigQuery.insertAll(
                InsertAllRequest.newBuilder(tableId)
                    .setIgnoreUnknownValues(true)
                    .setSkipInvalidRows(true)
                    .setRows(rowsToInsert)
                    .build()
            );
            if (response.hasErrors()) {
                for (Map.Entry<Long, List<com.google.cloud.bigquery.BigQueryError>> entry : response.getInsertErrors().entrySet()) {
                    Logger.log(String.format("Response error: %s", entry.getValue()), Logger.LogType.ERROR);
                }
                return Collections.emptyList();
            } else {
                Logger.log("Rows successfully inserted into table", Logger.LogType.INFO);
                return rowsToInsert;
            }
        } catch (BigQueryException e) {
            Logger.log("Insert operation not performed.", Logger.LogType.ERROR);
            Logger.log(String.format("%s", e), Logger.LogType.ERROR);
        }
        return Collections.emptyList();
    }

    public ResponseEntity<ExampleResponse> query(Query query, String gcpToken) throws IOException {
        HttpHeaders httpHeaders = getHttpHeaders(gcpToken);
        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(query), httpHeaders);
        log.info("{}", query.getQuery());
        long requestTime = System.currentTimeMillis();
        try {
            return bigQueryHttpUtil.callBigQueryApi(bigQueryConfig, request, mapper);
        } catch (HttpClientErrorException e) {
            return handleHttpClientErrorException(e, requestTime);
        } catch (IOException e) {
            return handleIOException(e);
        }
    }

    public ResponseEntity<ExampleResponse> query(ExampleRequest request, String gcpToken) throws IOException {
        List<ExampleRequestItem> subqueries = request.getSubqueries();
        if (subqueries.isEmpty()) {
            return new ResponseEntity<>(ExampleResponse.builder().build(), HttpStatus.OK);
        }

        final int batchSize = 200;
        ExampleResponse response = BigQueryHttpUtil.initExampleResponse();
        for (int i = 0; i < subqueries.size(); i += batchSize) {
            List<ExampleRequestItem> batch = subqueries.subList(i, Math.min(i + batchSize, subqueries.size()));
            ResponseEntity<ExampleResponse> responseEntity = query(
                Query.builder()
                    .query(buildQueryString(ExampleRequest.builder().subqueries(batch).build()))
                    .useLegacySql(false)
                    .build(),
                gcpToken
            );
            if (!BigQueryHttpUtil.isOkExampleResponse(responseEntity)) {
                return buildErrorExampleResponse(responseEntity);
            } else {
                ExampleResponse body = responseEntity.getBody();
                if (null != body && null != body.getRows()) {
                    List<ExampleResponseItem> responseBody = body.getRows();
                    int numberOfRows = responseBody.size();
                    if (shouldUploadBqResponseToGcs && responseSizeLimit >= numberOfRows) {
                        URL url = gcsService.upload(responseBody);
                        response.setQueryUrl(url.toString());
                    } else if (responseSizeLimit < numberOfRows) {
                        log.error("Number of rows in BQ response exceeded size limit.");
                        log.error("TODO: Implement logic to handle this scenario.");
                    } else {
                        response.getRows().addAll(responseBody);
                    }
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity<>(
            ExampleResponse.builder().rows(new ArrayList<>()).build(),
            HttpStatus.ACCEPTED
        );
    }

    private void handleResponse(ResponseEntity<ExampleResponse> responseEntity, ExampleResponse response) {
    }

    private ResponseEntity<ExampleResponse> handleIOException(IOException e) {
        return new ResponseEntity<>(
            ExampleResponse.builder()
                .errors(Collections.singletonList(e.getMessage()))
                .build(),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<ExampleResponse> handleHttpClientErrorException(HttpClientErrorException e, long requestTime) {
        float duration = (System.currentTimeMillis() - requestTime) / 1000f;
        log.error("query(): Received response after {} seconds", String.format("%.5f", duration));
        String errorMessage = e.getMessage();
        log.error(errorMessage);
        return buildClientErrorExampleResponse(e);
    }

    private String buildQueryString(ExampleRequest request) throws IOException {
        return bigQueryUtil.buildQueryString(
            "query_1",
            request,
            bigQueryConfig
        );
    }

    private ResponseEntity<ExampleResponse> buildErrorExampleResponse(ResponseEntity<ExampleResponse> responseEntity) {
        ExampleResponse.ExampleResponseBuilder responseBuilder = ExampleResponse.builder();
        ExampleResponse responseBody = responseEntity.getBody();
        if (null != responseBody) {
            responseBuilder.errors(responseBody.getErrors());
        }
        return new ResponseEntity<>(responseBuilder.build(), responseEntity.getStatusCode());
    }

    private ResponseEntity<ExampleResponse> buildClientErrorExampleResponse(HttpClientErrorException e) {
        return new ResponseEntity<>(
            ExampleResponse.builder()
                .errors(Collections.singletonList(e.getMessage()))
                .build(),
            e.getStatusCode()
        );
    }

    /**
     * <a href="https://cloud.google.com/bigquery/docs/running-queries#queries">Run an interactive query</a>
     *
     * @param query A Google SQL query string.
     * @return TableResult The rows returned from the query.
     */
    public TableResult query(String gcpAccessToken, String query) throws IOException {
        try {
            // Set optional job resource properties.
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query)
                .setLabels(ImmutableMap.of("example-label", "example-value"))
                .build();

            // The location and job name are optional, if both are not specified then client will auto-create.
            String jobName = "jobId_" + UUID.randomUUID();
            JobId jobId = JobId.newBuilder().setLocation("us").setJob(jobName).build();

//            Logger.log(String.format("GCP ACCESS TOKEN == %s", gcpAccessToken), Logger.LogType.CYAN);
            bigQueryConfig.setGcpCredentials(gcpAccessToken);

            bigQuery.create(JobInfo.of(jobId, queryConfig));  // Create a job with job ID.

            Job job = bigQuery.getJob(jobId);  // Get the job that was just created.
            String _job = job.getJobId().getJob();
            TableResult tableResult;
            if (null != _job && _job.equals(jobId.getJob())) {
                Logger.log(
                    String.format("%s: Job \"%s\" was created successfully.", this.getClass().getName(), _job),
                    Logger.LogType.INFO
                );
                job.waitFor();
                if (null != job.getStatus().getError()) {
                    throw new CustomJobException(job.getStatus().getError().getMessage());
                }
            } else {
                Logger.log("Job was not created.", Logger.LogType.ERROR);
            }
            tableResult = job.getQueryResults();
            Logger.log("Query performed successfully.", Logger.LogType.INFO);
            return tableResult;
        } catch (BigQueryException | InterruptedException | CustomJobException e) {
            Logger.log(
                String.format("%s: Job \"%s\" was not created.", e.getClass().getName(), e.getMessage()),
                Logger.LogType.ERROR
            );
        }
        return new EmptyTableResult(Schema.of());
    }

    public TableResult query(String gcpToken, ExampleRequest request) throws IOException {
        String queryString = bigQueryUtil.buildQueryString("query_1", request, bigQueryConfig);
        return query(gcpToken, queryString);
    }

    /**
     * <a href="https://cloud.google.com/bigquery/docs/running-queries#batch">Run a batch query</a>
     *
     * @param query A Google SQL query string.
     * @return TableResult The rows returned from the query.
     */
    public TableResult queryBatch(String query) {
        try {
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query)
                // Run at batch priority, which won't count toward concurrent rate limit.
                .setPriority(QueryJobConfiguration.Priority.BATCH)
                .build();
            Logger.log("Query batch performed successfully.", Logger.LogType.INFO);
            return bigQuery.query(queryConfig);
        } catch (BigQueryException | InterruptedException e) {
            Logger.log("Query batch not performed:", Logger.LogType.ERROR);
            Logger.log(String.format("%s", e.getMessage()), Logger.LogType.ERROR);
        }
        return null;
    }

    /**
     * <a href="https://cloud.google.com/bigquery/docs/dry-run-queries#perform_dry_runs">Perform a query dry run</a>
     *
     * @param query A Google SQL query string.
     */
    public void queryDryRun(String query) {
        try {
            QueryJobConfiguration queryConfig =
                QueryJobConfiguration.newBuilder(query).setDryRun(true).setUseQueryCache(false).build();
            Job job = bigQuery.create(JobInfo.of(queryConfig));
            JobStatistics.QueryStatistics statistics = job.getStatistics();
            Logger.log(
                "Query dry run performed successfully. Total bytes processed: " + statistics.getTotalBytesProcessed(),
                Logger.LogType.INFO
            );
        } catch (BigQueryException e) {
            Logger.log("Query not performed. " + e, Logger.LogType.ERROR);
        }
    }


    private HttpHeaders getHttpHeaders(String gcpAccessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        String systemArgGcpAccessToken = System.getProperty("GCP_ACCESS_TOKEN");

        // By default, use the access token passed from HTTP headers. Give priority to the GCP_ACCESS_TOKEN system argument.
        if (StringUtils.isNotEmpty(systemArgGcpAccessToken)) {
            if (StringUtils.isNotEmpty(gcpAccessToken)) {
                httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer ".concat(gcpAccessToken));
            } else {
                httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer ".concat(systemArgGcpAccessToken));
            }
        }
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return httpHeaders;
    }

}
