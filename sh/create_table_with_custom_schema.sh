#!/bin/bash

./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryServiceEndToEndTest.createTableWithCustomSchema \
  -DPROFILE=integration \
  -DGCP_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_DATASET="test_dataset_integration" \
  -DGCP_DEFAULT_TABLE="test_table_integration" \
  -DSCHEMA="id,string;fieldA,string;fieldB,string"
