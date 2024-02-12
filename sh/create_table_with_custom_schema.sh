#!/bin/bash

./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryServiceEndToEndTest.createTableWithCustomSchema \
  -DAPP_PROFILE=integration \
  -DGCP_PROJECT_ID=${GCP_PROJECT_ID} \
  -DBQ_DATASET=${BQ_DATASET} \
  -DBQ_TABLE=${BQ_TABLE} \
  -DSCHEMA="id,string;fieldA,string;fieldB,string"
