#!/bin/bash

./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryAdminClientIntegrationTest.createDataset \
  -Dprofile=integration \
  -DprojectId="lofty-root-378503" \
  -DdatasetName="test_dataset_name_integration"
