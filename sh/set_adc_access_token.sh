#!/bin/bash

export GOOGLE_APPLICATION_CREDENTIALS="$(gcloud auth application-default print-access-token)"
