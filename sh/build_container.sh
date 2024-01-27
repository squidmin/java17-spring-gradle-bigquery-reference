#!/bin/bash

docker build \
  --build-arg GCP_DEFAULT_PROJECT_ID=lofty-root-378503 \
  -t java11-spring-gradle-bigquery-reference .
