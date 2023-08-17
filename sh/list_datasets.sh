#!/bin/bash

./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.spring.rest.springrestlabs.service.BigQueryAdminClientIntegrationTest.listDatasets \
  -Dprofile=integration \
  -DprojectId="lofty-root-378503"
