# java17-spring-gradle-bigquery-reference

[![Java CI with Gradle](https://github.com/squidmin/java17-spring-gradle-bigquery-reference/actions/workflows/spring-boot-ci.yml/badge.svg)](https://github.com/squidmin/java17-spring-gradle-bigquery-reference/actions/workflows/spring-boot-ci.yml)

<details>
<summary>About</summary>

Made with:
- **Adoptium Temurin OpenJDK 17.0.8**
- **Spring Boot v3.1.2**
- **Gradle 8.2.1**
- **IntelliJ IDEA 2023.1 (Ultimate Edition)**

</details>


---


## Install & build

<details>
<summary>Download, install, and initialize the gcloud SDK on your local machine</summary>

Refer to the <a href="https://cloud.google.com/sdk/docs/install#other_installation_options">`gcloud` CLI documentation</a> to complete this step.

Install the `gcloud` SDK to the user's home directory (e.g., `/Users/USERNAME/google-cloud-sdk`).

When it's finished installing, add the `gcloud` executable to your system's `$PATH` and run the command:

```shell
gcloud init
```

</details>


<details>
<summary>gcloud CLI: Application Default Credentials (ADC) usage</summary>

```shell
gcloud auth login
gcloud auth application-default login
```

</details>


<details>
<summary>gcloud CLI: Generate an Application Default Credentials (ADC) access token</summary>

If you're running the application locally, you can use the following command to generate an access token using Application Default Credentials (ADC):

```shell
gcloud auth application-default print-access-token
```

```shell
export GCP_ADC_ACCESS_TOKEN="$(gcloud auth application-default print-access-token)"
```

</details>


<details>
<summary>gcloud CLI: Generate an access token for service account impersonation</summary>

Run this command to generate an access token for a specific GCP service account:

```shell
export GCP_SA_ACCESS_TOKEN=$(gcloud auth print-access-token --impersonate-service-account='GCP_SA_EMAIL_ADDRESS')
```

**Replace the following**:
- `GCP_SA_EMAIL_ADDRESS`: the email address of the service account to impersonate.

Example:

```shell
export GCP_SA_ACCESS_TOKEN=$(gcloud auth print-access-token --impersonate-service-account='sa-developer@your-sa-name.iam.gserviceaccount.com')
```

</details>


<details>
<summary>Create and store a service account key</summary>

This section refers to usage of a GCP service account key (.json) file stored on your local file system.

To map a local `gcloud` installation to a volume on a container instance running the application, include the `-v` parameter in the `docker run` command used to start a container instance, as described below.

### macOS

Assuming the user's service account key file is stored in the same directory as their local `gcloud` installation:

`/Users/USERNAME/.config/gcloud`

```shell
export LOCAL_GCLOUD_AUTH_DIRECTORY=$HOME/.config/gcloud
```

and the target volume on the container instance is:

`/root/.config/gcloud`

```shell
export CONTAINER_GCLOUD_AUTH_DIRECTORY=/root/.config/gcloud
```

the command to run the container instance would be:

```shell
docker run --rm -it \
  -e GCP_SA_KEY_PATH=$GCP_SA_KEY_PATH \
  -e GCP_ADC_ACCESS_TOKEN=$GCP_ADC_ACCESS_TOKEN \
  -e GCP_SA_ACCESS_TOKEN=$GCP_SA_ACCESS_TOKEN \
  -e GCP_DEFAULT_USER_PROJECT_ID=$GCP_DEFAULT_USER_PROJECT_ID \
  -e GCP_DEFAULT_USER_DATASET=$GCP_DEFAULT_USER_DATASET \
  -e GCP_DEFAULT_USER_TABLE=$GCP_DEFAULT_USER_TABLE \
  -e GCP_SA_PROJECT_ID=$GCP_SA_PROJECT_ID \
  -e GCP_SA_DATASET=$GCP_SA_DATASET \
  -e GCP_SA_TABLE=$GCP_SA_TABLE \
  -v ${LOCAL_GCLOUD_AUTH_DIRECTORY}:${CONTAINER_GCLOUD_AUTH_DIRECTORY} \
  -v ${LOCAL_MAVEN_REPOSITORY}:${CONTAINER_MAVEN_REPOSITORY} \
  java17-spring-gradle-bigquery-reference
```

**Replace the following** in the path to the `gcloud` directory:

- `USERNAME`: the current OS user's username

so that the path to the service account key file is correct, e.g.:

`/Users/squidmin/.config/gcloud/sa-private-key.json`

Read <a href="https://cloud.google.com/iam/docs/keys-create-delete#iam-service-account-keys-create-gcloud">here</a> for more information about creating service account keys.

Read <a href="">here</a> for more information about run config CLI arguments.

</details>


<details>
<summary>Activate GCP service account</summary>

```shell
gcloud auth activate-service-account --key-file=GCP_SA_KEY_PATH
```

**Replace the following**:
- `GCP_SA_KEY_PATH`: path to the user's service account key file.

Example:

```shell
gcloud auth activate-service-account --key-file='/Users/squidmin/.config/gcloud/sa-private-key.json'
```

</details>


<details>
<summary>Set the active GCP project</summary>

```shell
gcloud config set project ${GCP_DEFAULT_USER_PROJECT_ID}
```

</details>


<details>
<summary>List available gcloud SDK components</summary>

```shell
gcloud components list
```

</details>


<details>
<summary>Update gcloud SDK components</summary>

```shell
gcloud components update
```

</details>


<details>
<summary>CLI reference table: Run configuration</summary>



</details>


<details>
<summary>Build JAR</summary>

```shell
./gradlew clean build
```

```shell
./gradlew clean build -x test
```

```shell
./gradlew clean build testClasses -x test
```

</details>


<details>
<summary>Add manifest file</summary>

```shell
jar -cmvf \
  ./build/tmp/jar/MANIFEST.MF \
  ./build/libs/java17-spring-gradle-bigquery-reference-0.0.1-SNAPSHOT.jar \
  ./build/classes/java/main/org/squidmin/java/spring/gradle/bigquery/Java17SpringGradleBigQueryReferenceApplication.class
```

</details>


<details>
<summary>Build container image</summary>

```shell
docker build \
  --build-arg GCP_SA_KEY_PATH=${GCP_SA_KEY_PATH} \
  --build-arg GCP_DEFAULT_USER_PROJECT_ID=${GCP_DEFAULT_USER_PROJECT_ID} \
  --build-arg GCP_DEFAULT_USER_DATASET=${GCP_DEFAULT_USER_DATASET} \
  --build-arg GCP_DEFAULT_USER_TABLE=${GCP_DEFAULT_USER_TABLE}
  -t java17-spring-gradle-bigquery-reference .
```



</details>


---


## Commands


### Java application

<details>
<summary>Run container</summary>

```shell
docker run --rm -it \
  -e GCP_SA_KEY_PATH=$GCP_SA_KEY_PATH \
  -e GCP_ADC_ACCESS_TOKEN=$GCP_ADC_ACCESS_TOKEN \
  -e GCP_SA_ACCESS_TOKEN=$GCP_SA_ACCESS_TOKEN \
  -e GCP_DEFAULT_USER_PROJECT_ID=$GCP_DEFAULT_USER_PROJECT_ID \
  -e GCP_DEFAULT_USER_DATASET=$GCP_DEFAULT_USER_DATASET \
  -e GCP_DEFAULT_USER_TABLE=$GCP_DEFAULT_USER_TABLE \
  -e GCP_SA_PROJECT_ID=$GCP_SA_PROJECT_ID \
  -e GCP_SA_DATASET=$GCP_SA_DATASET \
  -e GCP_SA_TABLE=$GCP_SA_TABLE \
  -v ${LOCAL_GCLOUD_AUTH_DIRECTORY}:${CONTAINER_GCLOUD_AUTH_DIRECTORY} \
  -v ${LOCAL_MAVEN_REPOSITORY}:${CONTAINER_MAVEN_REPOSITORY} \
  java17-spring-gradle-bigquery-reference
```

</details>


<details>
<summary>Run jar</summary>

```shell
exec java -jar \
  -Dspring.profiles.active=local \
  ./build/libs/java17-spring-gradle-bigquery-reference-0.0.1-SNAPSHOT.jar
```

</details>


### `bq` CLI

<details>
<summary>List datasets</summary>

```shell
bq ls --filter labels.key:value \
  --max_results integer \
  --format=prettyjson \
  --project_id project_id
```

**Replace the following**:
- `key:value`: a label key and value, if applicable.
- `integer`: an integer representing the number of datasets to list.
- `project_id`: the name of the GCP project to target.

**Examples**:

```shell
bq ls --format=pretty
```

</details>


<details>
<summary>Create a dataset</summary>

Refer to the <a href="https://cloud.google.com/bigquery/docs/datasets#create-dataset">GCP documentation for creating datasets</a>.

**Examples**:

```shell
bq --location=us mk \
  --dataset \
  --default_partition_expiration=3600 \
  --default_table_expiration=3600 \
  --description="An example." \
  --label=test_label_1:test_value_1 \
  --label=test_label_2:test_value_2 \
  --max_time_travel_hours=168 \
  --storage_billing_model=LOGICAL \
  lofty-root-378503:test_dataset_integration
```

The Cloud Key Management Service (KMS) key parameter (`KMS_KEY_NAME`) can be specified.
This parameter is used to pass the name of the default Cloud Key Management Service key used to protect newly created tables in this dataset.
You cannot create a Google-encrypted table in a dataset with this parameter set.

```shell
bq --location=us mk \
  --dataset \
  --default_kms_key=KMS_KEY_NAME \
  ...
  lofty-root-378503:test_dataset_integration
```

</details>


<details>
<summary>Delete a dataset</summary>

Refer to the <a href="https://cloud.google.com/bigquery/docs/managing-datasets#delete_a_dataset">GCP documentation for deleting a dataset</a>.

#### Examples:

Remove all tables in the dataset (`-r` flag):

```shell
bq rm -r -f -d lofty-root-378503:test_dataset_integration
```

</details>


<details>
<summary>Create a table with a configured schema</summary>

**Create an empty table with an inline schema definition**

```shell
bq mk --table project_id:dataset.table schema
```

**Replace the following**:
- `project_id`: the name of the GCP project to target.
- `dataset`: the name of the BigQuery dataset to target.
- `table`: the name of the BigQuery table to target.
- `schema`: an inline schema definition.

Example:

```shell
bq mk --table \
  lofty-root-378503:test_dataset_integration.test_table_integration \
  id:STRING,creation_timestamp:DATETIME,last_update_timestamp:DATETIME,column_a:STRING,column_b:BOOL
```

### Specify the schema in a JSON schema file

For an example JSON schema file, refer to: `/schema/example.json`.

**Create an empty table**

```shell
bq mk --table \
  project_id:dataset.table \
  path_to_schema_file
```

Example:

```shell
bq mk --table \
  lofty-root-378503:test_dataset_integration.test_table_integration \
  ./schema/example.json
```

**Create a table with CSV data**

```shell
bq --location=location load \
  --source_format=format \
  project_id:dataset.table \
  path_to_data_file \
  path_to_schema_file
```

Example:

```shell
bq --location=us load \
  --source_format=CSV \
  lofty-root-378503:test_dataset_integration.test_table_integration \
  ./csv/example.csv \
  ./schema/example.json
```

Refer to the BigQuery documentation: <a href="https://cloud.google.com/bigquery/docs/loading-data-cloud-storage-csv#details_of_loading_csv_data">Details of loading CSV data</a>.

</details>


<details>
<summary>Delete a table</summary>

```shell
bq rm --table test_dataset_integration.test_table_integration
```

</details>


<details>
<summary>Show table schema</summary>

Example:

```shell
bq show \
  --schema \
  --format=prettyjson \
  lofty-root-378503:test_dataset_integration.test_table_integration
```

The table schema can be written to a file:

```shell
bq show \
  --schema \
  --format=prettyjson \
  lofty-root-378503:test_dataset_integration.test_table_integration \ > ./schema/example_show-write.json
```

</details>


<details>
<summary>Modify table schemas</summary>

```shell
bq update \
  lofty-root-378503:test_dataset_integration.test_table_integration \
  ./schema/example_update.json
```

Refer to the <a href="https://cloud.google.com/bigquery/docs/managing-table-schemas">GCP documentation on modifying table schemas.</a>.

</details>


<details>
<summary>Insert data into a table</summary>

**Examples**:

Insert for known values:

```shell
bq insert test_dataset_integration.test_table_integration ./json/example.json
```

Specify a template suffix (`--template_suffix` or `-x`):

```shell
bq insert --ignore_unknown_values \
  --template_suffix=_insert \
  test_dataset_integration.test_table_integration \
  ./json/example.json
```

Refer to the <a href="">`bq insert` documentation</a>.

</details>


<details>
<summary>Run an interactive query</summary>

```shell
bq query \
  --use_legacy_sql=false \
  'query_string'
```

Example:

```shell
bq query \
  --use_legacy_sql=false \
  'SELECT
    id, fieldC
  FROM
    `lofty-root-378503.test_dataset_integration.test_table_integration`
  LIMIT
    3;'
```

</details>
