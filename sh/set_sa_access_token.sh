#!/bin/bash

GCP_SERVICE_ACCOUNT="$1"

export GOOGLE_APPLICATION_CREDENTIALS="$(gcloud auth print-access-token --impersonate-service-account=${GCP_SERVICE_ACCOUNT})"
