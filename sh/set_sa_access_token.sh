#!/bin/bash

export GOOGLE_APPLICATION_CREDENTIALS="$(gcloud auth print-access-token --impersonate-service-account='9644524330-compute@developer.gserviceaccount.com')"
