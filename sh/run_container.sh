#!/bin/bash

GOOGLE_APPLICATION_CREDENTIALS="$1"

docker run \
  --rm -it \
  -e GOOGLE_APPLICATION_CREDENTIALS=${GOOGLE_APPLICATION_CREDENTIALS} \
  -v $HOME/.config/gcloud:/root/.config/gcloud \
  -v $HOME/.m2:/root/.m2 \
  java17-spring-gradle-bigquery-reference
