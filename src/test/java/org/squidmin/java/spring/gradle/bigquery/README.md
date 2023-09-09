## Tests

### REST controller

<details>
<summary>Run the "query" endpoint test</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.controller.ControllerIntegrationTest.query_givenClientRequest_whenCalled_thenReturnOkResponse \
  -DPROFILE=integration \
  -DGCP_SA_KEY_PATH=$GCP_SA_KEY_PATH \
  -DGCP_ADC_ACCESS_TOKEN="$(gcloud auth application-default print-access-token)" \
  -DGCP_DEFAULT_USER_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_name_integration" \
  -DGCP_DEFAULT_USER_TABLE="test_table_name_integration"
```

</details>


### Table admin

<details>
<summary>List BigQuery resource metadata configured for a particular Spring profile</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientTest.echoBigQueryResourceConfig \
  -DPROFILE=PROFILE_NAME \
  -DGCP_DEFAULT_USER_PROJECT_ID=GCP_DEFAULT_USER_PROJECT_ID
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate for the method execution.
- `GCP_DEFAULT_USER_PROJECT_ID`: the project ID for the GCP project to target.

For example, assuming the name of the profile to activate is `integration`:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientTest.echoBigQueryResourceConfig \
  -DPROFILE=integration \
  -DGCP_DEFAULT_USER_PROJECT_ID=lofty-root-378503
```

</details>


<details>
<summary>List datasets</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientTest.listDatasets \
  -DPROFILE=PROFILE_NAME \
  -DGCP_DEFAULT_USER_PROJECT_ID=GCP_DEFAULT_USER_PROJECT_ID
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `GCP_DEFAULT_USER_PROJECT_ID`: the project ID of the GCP project to target.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientTest.listDatasets \
  -DPROFILE=integration \
  -DGCP_DEFAULT_USER_PROJECT_ID="lofty-root-378503"
```

</details>


<details>
<summary>Check whether a dataset exists</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientTest.datasetExists \
  -DPROFILE=PROFILE_NAME \
  -DGCP_DEFAULT_USER_PROJECT_ID="PROJECT_ID" \
  -DGCP_DEFAULT_USER_DATASET="GCP_DEFAULT_USER_DATASET"
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `GCP_DEFAULT_USER_PROJECT_ID`: the project ID of the GCP project to target.
- `GCP_DEFAULT_USER_DATASET`: the name of the dataset to target.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientTest.datasetExists \
  -DPROFILE=integration \
  -DGCP_DEFAULT_USER_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_name_integration"
```

</details>


<details>
<summary>Create a dataset</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientTest.createDataset \
  -DPROFILE=PROFILE_NAME \
  -DGCP_DEFAULT_USER_PROJECT_ID=GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET=GCP_DEFAULT_USER_DATASET
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `GCP_DEFAULT_USER_PROJECT_ID`: the project ID of the GCP project to target.
- `GCP_DEFAULT_USER_DATASET`: the name of the dataset to target.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientTest.createDataset \
  -DPROFILE=integration \
  -DGCP_DEFAULT_USER_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_name_integration"
```

</details>


<details>
<summary>Delete a dataset</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientTest.deleteDataset \
  -DPROFILE=PROFILE_NAME \
  -DGCP_DEFAULT_USER_PROJECT_ID=GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET=GCP_DEFAULT_USER_DATASET
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `GCP_DEFAULT_USER_PROJECT_ID`: the project ID of the GCP project to target.
- `GCP_DEFAULT_USER_DATASET`: the name of the dataset to target.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientTest.deleteDataset \
  -DPROFILE=integration \
  -DGCP_DEFAULT_USER_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_name_integration"
```

</details>


<details>
<summary>Delete dataset and contents</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientTest.deleteDatasetAndContents \
  -DPROFILE=PROFILE_NAME \
  -DGCP_DEFAULT_USER_PROJECT_ID=GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET=GCP_DEFAULT_USER_DATASET
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `GCP_DEFAULT_USER_PROJECT_ID`: the project ID of the GCP project to target.
- `GCP_DEFAULT_USER_DATASET`: the name of the dataset to target.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientTest.deleteDatasetAndContents \
  -Dprofile=integration \
  -DGCP_DEFAULT_USER_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_name_integration"
```

</details>


<details>
<summary>Create a table with the default schema</summary>

This command creates a table using the default schema configured in the Spring application.

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientTest.createTableWithDefaultSchema \
  -DPROFILE=PROFILE_NAME \
  -DGCP_DEFAULT_USER_PROJECT_ID=GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET=GCP_DEFAULT_USER_DATASET \
  -DGCP_DEFAULT_USER_TABLE=GCP_DEFAULT_USER_TABLE
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `GCP_DEFAULT_USER_PROJECT_ID`: the project ID of the GCP project to target.
- `GCP_DEFAULT_USER_DATASET`: the name of the BigQuery dataset to target.
- `GCP_DEFAULT_USER_TABLE`: the name of the BigQuery table to target.

Example using the `integration` profile:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientTest.createTableWithDefaultSchema \
  -DPROFILE=integration \
  -DGCP_DEFAULT_USER_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_name_integration" \
  -DGCP_DEFAULT_USER_TABLE="test_table_name_integration"
```

</details>


<details>
<summary>Create a table with a configured schema</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientTest.createTableWithCustomSchema \
  -DPROFILE=PROFILE_NAME \
  -DGCP_DEFAULT_USER_PROJECT_ID=GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET=GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_TABLE=GCP_DEFAULT_USER_TABLE \
  -DSCHEMA="name_1:datatype_1,name_2:datatype_2,[...],name_n:datatype_n"
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `GCP_DEFAULT_USER_PROJECT_ID`: the name of the GCP project ID to target.
- `GCP_DEFAULT_USER_DATASET`: the name of the BigQuery dataset to target.
- `GCP_DEFAULT_USER_TABLE`: the name of the BigQuery table to target.
- `name_1:datatype_1,name_2:datatype_2,[...],name_n:datatype_n`: a basic representation of a database schema.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientTest.createTableWithCustomSchema \
  -DPROFILE=integration \
  -DGCP_DEFAULT_USER_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_name_integration" \
  -DGCP_DEFAULT_USER_TABLE="test_table_name_integration" \
  -DSCHEMA="id:STRING,creation_timestamp:DATETIME,last_update_timestamp:DATETIME,column_a:STRING,column_b:BOOL"
```

</details>


<details>
<summary>Delete a table</summary>

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientTest.deleteTable \
  -Dprofile=PROFILE_NAME \
  -DGCP_DEFAULT_USER_PROJECT_ID=GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET=GCP_DEFAULT_USER_DATASET \
  -DGCP_DEFAULT_USER_TABLE=GCP_DEFAULT_USER_TABLE
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `GCP_DEFAULT_USER_PROJECT_ID`: the name of the GCP project ID to target.
- `GCP_DEFAULT_USER_DATASET`: the name of the BigQuery dataset to target.
- `GCP_DEFAULT_USER_TABLE`: the name of the BigQuery table to target.

Example:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientTest.deleteTable \
  -Dprofile=integration \
  -DGCP_DEFAULT_USER_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_name_integration" \
  -DGCP_DEFAULT_USER_TABLE="test_table_name_integration"
```

</details>


<details>
<summary>Insert rows</summary>

To test row insertion, run the following command:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.insert \
  -DPROFILE=PROFILE_NAME \
  -DGCP_DEFAULT_USER_PROJECT_ID=GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET=GCP_DEFAULT_USER_DATASET \
  -DGCP_DEFAULT_USER_TABLE=GCP_DEFAULT_USER_TABLE
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `GCP_DEFAULT_USER_PROJECT_ID`: the name of the GCP project ID to target.
- `GCP_DEFAULT_USER_DATASET`: the name of the BigQuery dataset to target.
- `GCP_DEFAULT_USER_TABLE`: the name of the BigQuery table to target.

Example using the `integration` profile:

```shell
./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.insert \
  -DPROFILE=integration \
  -DGCP_DEFAULT_USER_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_name_integration" \
  -DGCP_DEFAULT_USER_TABLE="test_table_name_integration"
```

</details>
