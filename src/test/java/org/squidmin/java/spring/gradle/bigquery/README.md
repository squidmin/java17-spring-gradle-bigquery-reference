## Tests

### REST controller

<details>
<summary>Run the "query" endpoint test</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.controller.BigQueryControllerIntegrationTest.query_givenClientRequest_whenCalled_thenReturnOkResponse \
  -DAPP_PROFILE=integration \
  -DGCP_SA_KEY_PATH=${GCP_SA_KEY_PATH} \
  -DGCP_ACCESS_TOKEN="$(gcloud auth application-default print-access-token)" \
  -DGCP_PROJECT_ID="example-project-id" \
  -DBQ_DATASET="test_dataset_integration" \
  -DBQ_TABLE="test_table_integration"
```

</details>


### Table admin

<details>
<summary>List BigQuery resource metadata configured for a particular Spring profile</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.echoBigQueryResourceConfig \
  -DAPP_PROFILE=${APP_PROFILE} \
  -DGCP_PROJECT_ID=${GCP_PROJECT_ID}
```

**Replace the following**:
- `APP_PROFILE`: the application profile.
- `GCP_PROJECT_ID`: the GCP project ID.

For example, assuming the name of the profile to activate is `integration`:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.echoBigQueryResourceConfig \
  -DAPP_PROFILE=integration \
  -DGCP_PROJECT_ID=example-project-id
```

</details>


<details>
<summary>List datasets</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.listDatasets \
  -DAPP_PROFILE=${APP_PROFILE} \
  -DGCP_PROJECT_ID=${GCP_PROJECT_ID}
```

**Replace the following**:
- `APP_PROFILE`: the application profile.
- `GCP_PROJECT_ID`: the GCP project ID.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.listDatasets \
  -DAPP_PROFILE=integration \
  -DGCP_PROJECT_ID="example-project-id"
```

</details>


<details>
<summary>Check whether a dataset exists</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.datasetExists \
  -DAPP_PROFILE=${APP_PROFILE} \
  -DGCP_PROJECT_ID=${GCP_PROJECT_ID} \
  -DBQ_DATASET=${BQ_DATASET}
```

**Replace the following**:
- `APP_PROFILE`: the application profile.
- `GCP_PROJECT_ID`: the GCP project ID.
- `BQ_DATASET`: the name of the BigQuery dataset.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.datasetExists \
  -DAPP_PROFILE=integration \
  -DGCP_PROJECT_ID="example-project-id" \
  -DBQ_DATASET="test_dataset_integration"
```

</details>


<details>
<summary>Create a dataset</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.createDataset \
  -DAPP_PROFILE=${APP_PROFILE} \
  -DGCP_PROJECT_ID=${GCP_PROJECT_ID} \
  -DBQ_DATASET=${BQ_DATASET}
```

**Replace the following**:
- `APP_PROFILE`: the application profile.
- `GCP_PROJECT_ID`: the GCP project ID.
- `BQ_DATASET`: the name of the BigQuery dataset.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.createDataset \
  -DAPP_PROFILE=integration \
  -DGCP_PROJECT_ID="example-project-id" \
  -DBQ_DATASET="test_dataset_integration"
```

</details>


<details>
<summary>Delete a dataset</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.deleteDataset \
  -DAPP_PROFILE=${APP_PROFILE} \
  -DGCP_PROJECT_ID=${GCP_PROJECT_ID} \
  -DBQ_DATASET=${BQ_DATASET}
```

**Replace the following**:
- `APP_PROFILE`: the application profile.
- `GCP_PROJECT_ID`: the GCP project ID.
- `BQ_DATASET`: the name of the BigQuery dataset.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.deleteDataset \
  -DAPP_PROFILE=integration \
  -DGCP_PROJECT_ID="example-project-id" \
  -DBQ_DATASET="test_dataset_integration"
```

</details>


<details>
<summary>Delete dataset and contents</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.deleteDatasetAndContents \
  -DAPP_PROFILE=${APP_PROFILE} \
  -DGCP_PROJECT_ID=${GCP_PROJECT_ID} \
  -DBQ_DATASET=${BQ_DATASET}
```

**Replace the following**:
- `APP_PROFILE`: the application profile.
- `GCP_PROJECT_ID`: the GCP project ID.
- `BQ_DATASET`: the name of the BigQuery dataset.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.deleteDatasetAndContents \
  -DAPP_PROFILE=integration \
  -DGCP_PROJECT_ID="example-project-id" \
  -DBQ_DATASET="test_dataset_integration"
```

</details>


<details>
<summary>Create a table with the default schema</summary>

This command creates a table using the default schema configured in the Spring application.

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.createTableWithDefaultSchema \
  -DAPP_PROFILE=${APP_PROFILE} \
  -DGCP_PROJECT_ID=${GCP_PROJECT_ID} \
  -DBQ_DATASET=${BQ_DATASET} \
  -DBQ_TABLE=${BQ_TABLE}
```

**Replace the following**:
- `APP_PROFILE`: the application profile.
- `GCP_PROJECT_ID`: the GCP project ID.
- `BQ_DATASET`: the name of the BigQuery dataset.
- `BQ_TABLE`: the name of the BigQuery table.

Example using the `integration` profile:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.createTableWithDefaultSchema \
  -DAPP_PROFILE=integration \
  -DGCP_PROJECT_ID="example-project-id" \
  -DBQ_DATASET="test_dataset_integration" \
  -DBQ_TABLE="test_table_integration"
```

</details>


<details>
<summary>Create a table with a configured schema</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.createTableWithCustomSchema \
  -DAPP_PROFILE=${APP_PROFILE} \
  -DGCP_PROJECT_ID=${GCP_PROJECT_ID} \
  -DBQ_DATASET=${BQ_DATASET} \
  -DBQ_TABLE=${BQ_TABLE} \
  -DSCHEMA="name_1:datatype_1,name_2:datatype_2,[...],name_n:datatype_n"
```

**Replace the following**:
- `APP_PROFILE`: the application profile.
- `GCP_PROJECT_ID`: the GCP project ID.
- `BQ_DATASET`: the name of the BigQuery dataset.
- `BQ_TABLE`: the name of the BigQuery table.
- `name_1:datatype_1,name_2:datatype_2,[...],name_n:datatype_n`: a database schema declaration.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.createTableWithCustomSchema \
  -DAPP_PROFILE=integration \
  -DGCP_PROJECT_ID="example-project-id" \
  -DBQ_DATASET="test_dataset_integration" \
  -DBQ_TABLE="test_table_integration" \
  -DSCHEMA="id:STRING,creation_timestamp:DATETIME,last_update_timestamp:DATETIME,column_a:STRING,column_b:BOOL"
```

</details>


<details>
<summary>Delete a table</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.deleteTable \
  -DAPP_PROFILE=${APP_PROFILE} \
  -DGCP_PROJECT_ID=${GCP_PROJECT_ID} \
  -DBQ_DATASET=${BQ_DATASET} \
  -DBQ_TABLE=${BQ_TABLE}
```

**Replace the following**:
- `APP_PROFILE`: the application profile.
- `GCP_PROJECT_ID`: the GCP project ID.
- `BQ_DATASET`: the name of the BigQuery dataset.
- `BQ_TABLE`: the name of the BigQuery table.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.deleteTable \
  -DAPP_PROFILE=integration \
  -DGCP_PROJECT_ID="example-project-id" \
  -DBQ_DATASET="test_dataset_integration" \
  -DBQ_TABLE="test_table_integration"
```

</details>


<details>
<summary>Insert rows</summary>

To test row insertion, run the following command:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.insert \
  -DAPP_PROFILE=APP_PROFILE \
  -DGCP_PROJECT_ID=${GCP_PROJECT_ID} \
  -DBQ_DATASET=${BQ_DATASET} \
  -DBQ_TABLE=${BQ_TABLE}
```

**Replace the following**:
- `APP_PROFILE`: the application profile.
- `GCP_PROJECT_ID`: the GCP project ID.
- `BQ_DATASET`: the name of the BigQuery dataset.
- `BQ_TABLE`: the name of the BigQuery table.

Example using the `integration` profile:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.insert \
  -DAPP_PROFILE=integration \
  -DGCP_PROJECT_ID="example-project-id" \
  -DBQ_DATASET="test_dataset_integration" \
  -DBQ_TABLE="test_table_integration"
```

</details>
