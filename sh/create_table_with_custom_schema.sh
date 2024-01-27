#!/bin/bash

./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryServiceEndToEndTest.createTableWithCustomSchema \
  -DPROFILE=integration \
  -DPROJECT_ID="lofty-root-378503" \
  -DDATASET_NAME="test_dataset_integration" \
  -DTABLE_NAME="test_table_integration" \
  -DSCHEMA="id,string;fieldA,string;fieldB,string"
