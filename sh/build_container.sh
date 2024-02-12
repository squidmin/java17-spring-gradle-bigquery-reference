#!/bin/bash

docker build \
  --build-arg GCP_PROJECT_ID=${GCP_PROJECT_ID} \
  -t java17-spring-gradle-bigquery-reference .
