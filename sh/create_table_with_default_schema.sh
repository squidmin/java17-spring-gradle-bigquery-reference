#!/bin/bash

./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryServiceEndToEndTest.createTableWithDefaultSchema \
  -DPROFILE=integration \
  -DGCP_DEFAULT_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_DATASET="test_dataset_name_integration" \
  -DGCP_DEFAULT_TABLE="test_table_name_integration_123"
