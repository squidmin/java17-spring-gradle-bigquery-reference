#!/bin/bash

./gradlew cleanTest test \
  -Dprofile=integration \
  --no-build-cache --tests \
  org.squidmin.java.spring.gradle.bigquery.controller.ControllerTest.lookUpById_givenClientRequest_whenCalled_thenReturn200 \
  -DprojectId="lofty-root-378503" \
  -DdatasetName="test_dataset_name_integration" \
  -DtableName="test_table_name_integration" \
  -Did=asdf-1234
