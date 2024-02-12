#!/bin/bash

GCP_PROJECT_ID="$1"
BQ_DATASET="$2"
BQ_TABLE="$3"

./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryServiceEndToEndTest.createTableWithDefaultSchema \
  -DAPP_PROFILE=integration \
  -DGCP_PROJECT_ID=${GCP_PROJECT_ID} \
  -DBQ_DATASET=${BQ_DATASET} \
  -DBQ_TABLE=${BQ_TABLE}
