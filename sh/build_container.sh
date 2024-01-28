#!/bin/bash

docker build \
  --build-arg GCP_DEFAULT_PROJECT_ID=lofty-root-378503 \
  -t java17-spring-gradle-bigquery-reference .
