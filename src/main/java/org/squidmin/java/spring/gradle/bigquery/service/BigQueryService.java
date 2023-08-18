package org.squidmin.java.spring.gradle.bigquery.service;

import autovalue.shaded.com.google.common.collect.ImmutableMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.paging.Page;
import com.google.cloud.bigquery.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryOptionsConfig;
import org.squidmin.java.spring.gradle.bigquery.config.DataTypes;
import org.squidmin.java.spring.gradle.bigquery.config.Exclusions;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SchemaDefault;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.SelectFieldsDefault;
import org.squidmin.java.spring.gradle.bigquery.config.tables.sandbox.WhereFieldsDefault;
import org.squidmin.java.spring.gradle.bigquery.dao.RecordExample;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleRequest;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponse;
import org.squidmin.java.spring.gradle.bigquery.dto.Query;
import org.squidmin.java.spring.gradle.bigquery.dto.bigquery.BigQueryRestServiceResponse;
import org.squidmin.java.spring.gradle.bigquery.dto.bigquery.error.BigQueryError;
import org.squidmin.java.spring.gradle.bigquery.exception.CustomJobException;
import org.squidmin.java.spring.gradle.bigquery.logger.Logger;
import org.squidmin.java.spring.gradle.bigquery.util.BigQueryUtil;
import org.squidmin.java.spring.gradle.bigquery.util.LoggerUtil;
import org.squidmin.java.spring.gradle.bigquery.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@EnableConfigurationProperties(value = {
    SchemaDefault.class,
    SelectFieldsDefault.class,
    WhereFieldsDefault.class,
    DataTypes.class,
    Exclusions.class,
})
@Slf4j
public class BigQueryService {

    private final String gcpDefaultUserProjectId;
    private final String gcpDefaultUserDataset;
    private final String gcpDefaultUserTable;
    private final String gcpSaProjectId;
    private final String gcpSaDataset;
    private final String gcpSaTable;

    private final BigQueryUtil bigQueryUtil;

    private final BigQuery bigQuery;

    private final BigQueryConfig bigQueryConfig;

    private final RestTemplate restTemplate;

    private final ObjectMapper mapper;

    @Autowired
    public BigQueryService(BigQueryUtil bigQueryUtil, BigQueryConfig bigQueryConfig, RestTemplate restTemplate) {
        this.bigQueryConfig = bigQueryConfig;
        this.bigQuery = bigQueryConfig.getBigQueryOptionsConfig().getBigQuery();
        this.gcpDefaultUserProjectId = bigQueryConfig.getGcpDefaultUserProjectId();
        this.gcpDefaultUserDataset = bigQueryConfig.getGcpDefaultUserDataset();
        this.gcpDefaultUserTable = bigQueryConfig.getGcpDefaultUserTable();
        this.gcpSaProjectId = bigQueryConfig.getGcpSaProjectId();
        this.gcpSaDataset = bigQueryConfig.getGcpSaDataset();
        this.gcpSaTable = bigQueryConfig.getGcpSaTable();
        this.bigQueryUtil = bigQueryUtil;
        this.restTemplate = restTemplate;
        mapper = new ObjectMapper();
    }

    /**
     * <a href="https://cloud.google.com/bigquery/docs/listing-datasets#list_datasets">List datasets</a>
     */
    public List<String> listDatasets() {
        List<String> datasetsList = new ArrayList<>();
        try {
            Page<Dataset> datasets = bigQuery.listDatasets(gcpDefaultUserProjectId, BigQuery.DatasetListOption.pageSize(100));
            if (null == datasets) {
                Logger.log(
                    String.format("Dataset \"%s\" does not contain any models.", gcpDefaultUserDataset),
                    Logger.LogType.ERROR
                );
                return new ArrayList<>();
            }
            datasets.iterateAll().forEach(dataset -> datasetsList.add(dataset.getFriendlyName()));
            LoggerUtil.logDatasets(gcpDefaultUserProjectId, datasets);
        } catch (BigQueryException e) {
            Logger.log(
                String.format("Project \"%s\" does not contain any datasets.", gcpDefaultUserProjectId),
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
            DatasetId datasetId = DatasetId.of(gcpDefaultUserProjectId, gcpDefaultUserDataset);
            Dataset dataset = bigQuery.getDataset(datasetId);

            // View dataset properties
            String description = dataset.getDescription();
            Logger.log(description, Logger.LogType.INFO);

            // View tables in the dataset
            // For more information on listing tables see:
            // https://javadoc.io/static/com.google.cloud/google-cloud-bigquery/0.22.0-beta/com/google/cloud/bigquery/BigQuery.html
            Page<Table> tables = bigQuery.listTables(gcpDefaultUserDataset, BigQuery.TableListOption.pageSize(100));

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

    public ResponseEntity<ExampleResponse> query(Query query, String bqApiToken) throws IOException {
        HttpHeaders httpHeaders = getHttpHeaders(bqApiToken);
        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(query), httpHeaders);
        log.info("{}", query.getQuery());
        long requestTime = System.currentTimeMillis();
        try {
            return callBigQueryApi(request, requestTime);
        } catch (HttpClientErrorException e) {
            return handleHttpClientErrorException(e, requestTime);
        } catch (IOException e) {
            return handleIOException(e);
        }
    }

    private ResponseEntity<ExampleResponse> callBigQueryApi(HttpEntity<String> request, long requestTime) throws IOException {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(bigQueryConfig.getQueryUri(), request, String.class);
        long responseTime = System.currentTimeMillis();
        float duration = (responseTime - requestTime) / 1000f;
        log.info("query(): Received response after {} seconds", String.format("%.5f", duration));
        String responseBody = responseEntity.getBody();
        if (HttpStatus.OK != responseEntity.getStatusCode()) {
            return handleHttpStatusNotOk(responseEntity, responseBody);
        }
        BigQueryRestServiceResponse response = mapper.readValue(responseBody, BigQueryRestServiceResponse.class);
        if (isOkBigQueryApiResponse(responseBody, response)) {
            boolean selectAll = bigQueryConfig.isSelectAll();
            return buildOkExampleResponse(responseBody, selectAll);
        } else {
            log.error("Response body is empty");
            return new ResponseEntity<>(ExampleResponse.builder().body(new ArrayList<>()).build(), HttpStatus.ACCEPTED);
        }
    }

    private ResponseEntity<ExampleResponse> handleHttpStatusNotOk(
        ResponseEntity<String> bqApiResponseEntity,
        String responseBody) throws JsonProcessingException {

        ResponseEntity<ExampleResponse> exampleResponseEntity = new ResponseEntity<>(HttpStatus.OK);
        if (HttpStatus.OK != bqApiResponseEntity.getStatusCode()) {
            BigQueryError bqError = mapper.readValue(responseBody, BigQueryError.class);
            ExampleResponse response = ExampleResponse.builder().errors(new ArrayList<>()).build();
            bqError.getError().getErrors().forEach(err -> response.getErrors().add(err.toString()));
            exampleResponseEntity = new ResponseEntity<>(response, bqApiResponseEntity.getStatusCode());
        }
        return exampleResponseEntity;

    }

    public ResponseEntity<ExampleResponse> query(ExampleRequest request, String bqApiToken) throws IOException {
        if (request.getBody().isEmpty()) {
            return new ResponseEntity<>(ExampleResponse.builder().build(), HttpStatus.OK);
        }
        ExampleResponse response = initResponse();
        ResponseEntity<ExampleResponse> responseEntity = query(
            Query.builder().query(buildQueryString(request)).useLegacySql(false).build(),
            bqApiToken
        );
        if (!isOkExampleResponse(responseEntity)) {
            return buildErrorExampleResponse(responseEntity);
        } else {
            if (null != responseEntity.getBody() && null != responseEntity.getBody().getBody()) {
                response.getBody().addAll(responseEntity.getBody().getBody());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    private ExampleResponse initResponse() {
        ExampleResponse response = new ExampleResponse();
        response.setBody(new ArrayList<>());
        return response;
    }

    private boolean isOkExampleResponse(String responseBody, BigQueryRestServiceResponse response) {
        return null != responseBody && null != response.getRows() && !response.getRows().isEmpty();
    }

    private boolean isOkExampleResponse(ResponseEntity<ExampleResponse> responseEntity) {
        return HttpStatus.OK == responseEntity.getStatusCode() && null != responseEntity.getBody();
    }

    private boolean isOkBigQueryApiResponse(String responseBody, BigQueryRestServiceResponse response) {
        return null != responseBody && null != response.getRows() && !response.getRows().isEmpty();
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
            "template",
            request,
            bigQueryConfig
        );
    }

    private ResponseEntity<ExampleResponse> buildOkExampleResponse(String responseBody, boolean selectAll)
        throws IOException {

        return new ResponseEntity<>(
            ExampleResponse.builder()
                .body(
                    BigQueryUtil.toList(
                        responseBody.getBytes(StandardCharsets.UTF_8),
                        bigQueryConfig.getSelectFieldsDefault(),
                        selectAll
                    )
                )
                .build(),
            HttpStatus.OK
        );

    }

    private ResponseEntity<ExampleResponse> buildErrorExampleResponse(ResponseEntity<ExampleResponse> responseEntity) {
        return new ResponseEntity<>(
            ExampleResponse.builder()
                .errors(Objects.requireNonNull(responseEntity.getBody().getErrors()))
                .build(),
            responseEntity.getStatusCode()
        );
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
    public TableResult query(String query) {
        try {
            // Set optional job resource properties.
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query)
                .setLabels(ImmutableMap.of("example-label", "example-value"))
                .build();

            // The location and job name are optional, if both are not specified then client will auto-create.
            String jobName = "jobId_" + UUID.randomUUID();
            JobId jobId = JobId.newBuilder().setLocation("us").setJob(jobName).build();

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


    private HttpHeaders getHttpHeaders(String bqApiToken) {
        HttpHeaders httpHeaders = new HttpHeaders();

        BigQueryOptionsConfig bigQueryOptionsConfig = bigQueryConfig.getBigQueryOptionsConfig();
        String gcpAdcAccessToken = bigQueryOptionsConfig.getGcpAdcAccessToken();
        String gcpSaAccessToken = bigQueryOptionsConfig.getGcpSaAccessToken();
        if (StringUtils.isNotEmpty(gcpAdcAccessToken)) {
            httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer ".concat(gcpAdcAccessToken));
        } else if (!StringUtils.isEmpty(gcpSaAccessToken)) {
            httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer ".concat(gcpSaAccessToken));
        } else {
            httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer ".concat(bqApiToken));
        }
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return httpHeaders;
    }

}
