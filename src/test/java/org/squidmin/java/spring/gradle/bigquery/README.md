## Tests

### REST controller

<details>
<summary>Run the "query" endpoint test</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.controller.BigQueryControllerIntegrationTest.query_givenClientRequest_whenCalled_thenReturnOkResponse \
  -DPROFILE=integration \
  -DGCP_SA_KEY_PATH=${GCP_SA_KEY_PATH} \
  -DGCP_ACCESS_TOKEN="$(gcloud auth application-default print-access-token)" \
  -DGCP_DEFAULT_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_DATASET="test_dataset_integration" \
  -DGCP_DEFAULT_TABLE="test_table_integration"
```

</details>


### Table admin

<details>
<summary>List BigQuery resource metadata configured for a particular Spring profile</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.echoBigQueryResourceConfig \
  -DPROFILE=${PROFILE} \
  -DGCP_DEFAULT_PROJECT_ID=${GCP_DEFAULT_PROJECT_ID}
```

**Replace the following**:
- `PROFILE`: the application profile.
- `GCP_DEFAULT_PROJECT_ID`: the GCP project ID.

For example, assuming the name of the profile to activate is `integration`:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.echoBigQueryResourceConfig \
  -DPROFILE=integration \
  -DGCP_DEFAULT_PROJECT_ID=lofty-root-378503
```

</details>


<details>
<summary>List datasets</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.listDatasets \
  -DPROFILE=${PROFILE} \
  -DGCP_DEFAULT_PROJECT_ID=${GCP_DEFAULT_PROJECT_ID}
```

**Replace the following**:
- `PROFILE`: the application profile.
- `GCP_DEFAULT_PROJECT_ID`: the GCP project ID.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.listDatasets \
  -DPROFILE=integration \
  -DGCP_DEFAULT_PROJECT_ID="lofty-root-378503"
```

</details>


<details>
<summary>Check whether a dataset exists</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.datasetExists \
  -DPROFILE=${PROFILE} \
  -DGCP_DEFAULT_PROJECT_ID=${GCP_DEFAULT_PROJECT_ID} \
  -DGCP_DEFAULT_DATASET=${GCP_DEFAULT_DATASET}
```

**Replace the following**:
- `PROFILE`: the application profile.
- `GCP_DEFAULT_PROJECT_ID`: the GCP project ID.
- `GCP_DEFAULT_DATASET`: the name of the dataset.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.datasetExists \
  -DPROFILE=integration \
  -DGCP_DEFAULT_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_DATASET="test_dataset_integration"
```

</details>


<details>
<summary>Create a dataset</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.createDataset \
  -DPROFILE=${PROFILE} \
  -DGCP_DEFAULT_PROJECT_ID=${GCP_DEFAULT_PROJECT_ID} \
  -DGCP_DEFAULT_DATASET=${GCP_DEFAULT_DATASET}
```

**Replace the following**:
- `PROFILE`: the application profile.
- `GCP_DEFAULT_PROJECT_ID`: the GCP project ID.
- `GCP_DEFAULT_DATASET`: the name of the dataset.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.createDataset \
  -DPROFILE=integration \
  -DGCP_DEFAULT_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_DATASET="test_dataset_name_integration"
```

</details>


<details>
<summary>Delete a dataset</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.deleteDataset \
  -DPROFILE=${PROFILE} \
  -DGCP_DEFAULT_PROJECT_ID=${GCP_DEFAULT_PROJECT_ID} \
  -DGCP_DEFAULT_DATASET=${GCP_DEFAULT_DATASET}
```

**Replace the following**:
- `PROFILE`: the application profile.
- `GCP_DEFAULT_PROJECT_ID`: the GCP project ID.
- `GCP_DEFAULT_DATASET`: the name of the dataset.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.deleteDataset \
  -DPROFILE=integration \
  -DGCP_DEFAULT_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_DATASET="test_dataset_integration"
```

</details>


<details>
<summary>Delete dataset and contents</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.deleteDatasetAndContents \
  -DPROFILE=${PROFILE} \
  -DGCP_DEFAULT_PROJECT_ID=${GCP_DEFAULT_PROJECT_ID} \
  -DGCP_DEFAULT_DATASET=${GCP_DEFAULT_DATASET}
```

**Replace the following**:
- `PROFILE`: the application profile.
- `GCP_DEFAULT_PROJECT_ID`: the GCP project ID.
- `GCP_DEFAULT_DATASET`: the name of the dataset.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.deleteDatasetAndContents \
  -DPROFILE=integration \
  -DGCP_DEFAULT_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_DATASET="test_dataset_integration"
```

</details>


<details>
<summary>Create a table with the default schema</summary>

This command creates a table using the default schema configured in the Spring application.

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.createTableWithDefaultSchema \
  -DPROFILE=${PROFILE} \
  -DGCP_DEFAULT_PROJECT_ID=${GCP_DEFAULT_PROJECT_ID} \
  -DGCP_DEFAULT_DATASET=${GCP_DEFAULT_DATASET} \
  -DGCP_DEFAULT_TABLE=${GCP_DEFAULT_TABLE}
```

**Replace the following**:
- `PROFILE`: the application profile.
- `GCP_DEFAULT_PROJECT_ID`: the GCP project ID.
- `GCP_DEFAULT_DATASET`: the name of the BigQuery dataset.
- `GCP_DEFAULT_TABLE`: the name of the BigQuery table.

Example using the `integration` profile:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.createTableWithDefaultSchema \
  -DPROFILE=integration \
  -DGCP_DEFAULT_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_DATASET="test_dataset_integration" \
  -DGCP_DEFAULT_TABLE="test_table_integration"
```

</details>


<details>
<summary>Create a table with a configured schema</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.createTableWithCustomSchema \
  -DPROFILE=${PROFILE} \
  -DGCP_DEFAULT_PROJECT_ID=${GCP_DEFAULT_PROJECT_ID} \
  -DGCP_DEFAULT_DATASET=${GCP_DEFAULT_DATASET} \
  -DGCP_DEFAULT_TABLE=${GCP_DEFAULT_TABLE} \
  -DSCHEMA="name_1:datatype_1,name_2:datatype_2,[...],name_n:datatype_n"
```

**Replace the following**:
- `PROFILE`: the application profile.
- `GCP_DEFAULT_PROJECT_ID`: the GCP project ID.
- `GCP_DEFAULT_DATASET`: the name of the BigQuery dataset.
- `GCP_DEFAULT_TABLE`: the name of the BigQuery table.
- `name_1:datatype_1,name_2:datatype_2,[...],name_n:datatype_n`: a database schema declaration.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.createTableWithCustomSchema \
  -DPROFILE=integration \
  -DGCP_DEFAULT_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_DATASET="test_dataset_integration" \
  -DGCP_DEFAULT_TABLE="test_table_integration" \
  -DSCHEMA="id:STRING,creation_timestamp:DATETIME,last_update_timestamp:DATETIME,column_a:STRING,column_b:BOOL"
```

</details>


<details>
<summary>Delete a table</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.deleteTable \
  -DPROFILE=${PROFILE} \
  -DGCP_DEFAULT_PROJECT_ID=${GCP_DEFAULT_PROJECT_ID} \
  -DGCP_DEFAULT_DATASET=${GCP_DEFAULT_DATASET} \
  -DGCP_DEFAULT_TABLE=${GCP_DEFAULT_TABLE}
```

**Replace the following**:
- `PROFILE`: the application profile.
- `GCP_DEFAULT_PROJECT_ID`: the GCP project ID.
- `GCP_DEFAULT_DATASET`: the name of the BigQuery dataset.
- `GCP_DEFAULT_TABLE`: the name of the BigQuery table.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.deleteTable \
  -DPROFILE=integration \
  -DGCP_DEFAULT_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_DATASET="test_dataset_integration" \
  -DGCP_DEFAULT_TABLE="test_table_integration"
```

</details>


<details>
<summary>Insert rows</summary>

To test row insertion, run the following command:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.insert \
  -DPROFILE=PROFILE \
  -DGCP_DEFAULT_PROJECT_ID=${GCP_DEFAULT_PROJECT_ID} \
  -DGCP_DEFAULT_DATASET=${GCP_DEFAULT_DATASET} \
  -DGCP_DEFAULT_TABLE=${GCP_DEFAULT_TABLE}
```

**Replace the following**:
- `PROFILE`: the application profile.
- `GCP_DEFAULT_PROJECT_ID`: the GCP project ID.
- `GCP_DEFAULT_DATASET`: the name of the BigQuery dataset.
- `GCP_DEFAULT_TABLE`: the name of the BigQuery table.

Example using the `integration` profile:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.insert \
  -DPROFILE=integration \
  -DGCP_DEFAULT_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_DATASET="test_dataset_integration" \
  -DGCP_DEFAULT_TABLE="test_table_integration"
```

</details>
