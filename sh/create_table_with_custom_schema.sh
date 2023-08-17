#!/bin/bash

./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.createTableWithCustomSchema \
  -Dprofile=integration \
  -DprojectId="lofty-root-378503" \
  -DdatasetName="test_dataset_name_integration" \
  -DtableName="test_table_name_integration" \
  -Dschema="id,string;fieldA,string;fieldB,string"
