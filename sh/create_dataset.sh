#!/bin/bash

./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.createDataset \
  -DPROFILE=integration \
  -DGCP_DEFAULT_PROJECT_ID="lofty-root-378503" \
  -DGCP_DEFAULT_DATASET="test_dataset_integration"
