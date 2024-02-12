#!/bin/bash

GCP_PROJECT_ID="$1"
BQ_DATASET="$2"
BQ_TABLE="$3"

./gradlew cleanTest test \
  -DAPP_PROFILE=integration \
  --no-build-cache --tests \
  org.squidmin.java.spring.gradle.bigquery.controller.BigQueryControllerIntegrationTest.query_givenValidRequest_thenReturnHttpStatusOk_andResponseBodyIsNotNull \
  -DGCP_PROJECT_ID=${GCP_PROJECT_ID} \
  -DBQ_DATASET=${BQ_DATASET} \
  -DBQ_TABLE=${BQ_TABLE} \
  -Did=asdf-1234
