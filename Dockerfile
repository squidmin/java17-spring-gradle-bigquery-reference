FROM eclipse-temurin:17

### Build arguments ###
ARG APP_DIR
ARG APP_PROFILE
ARG GCP_SA_KEY_PATH
ARG GCP_ADC_ACCESS_TOKEN
ARG GCP_DEFAULT_USER_PROJECT_ID
ARG GCP_DEFAULT_USER_DATASET
ARG GCP_DEFAULT_USER_TABLE
ARG GCP_SA_PROJECT_ID
ARG GCP_SA_DATASET
ARG GCP_SA_TABLE
###

### Environment variables ###
# OS
ENV APP_DIR=${APP_DIR}
# JVM arguments.
ENV APP_PROFILE=${APP_PROFILE}
ENV GCP_SA_KEY_PATH=${GCP_SA_KEY_PATH}
ENV GCP_ADC_ACCESS_TOKEN=${GCP_ADC_ACCESS_TOKEN}
ENV GCP_DEFAULT_USER_PROJECT_ID=${GCP_DEFAULT_USER_PROJECT_ID}
ENV GCP_DEFAULT_USER_DATASET=${GCP_DEFAULT_USER_DATASET}
ENV GCP_DEFAULT_USER_TABLE=${GCP_DEFAULT_USER_TABLE}
ENV GCP_SA_PROJECT_ID=${GCP_SA_PROJECT_ID}
ENV GCP_SA_DATASET=${GCP_SA_DATASET}
ENV GCP_SA_TABLE=${GCP_SA_TABLE}
###

WORKDIR ${APP_DIR}

# Copy the project into the container.
COPY . .

#ENTRYPOINT ["sh", "-c", "cd ${APP_DIR} && sh"]
ENTRYPOINT ./gradlew bootRun -DPROFILE=${APP_PROFILE} -DGCP_DEFAULT_USER_PROJECT_ID=${GCP_DEFAULT_USER_PROJECT_ID} -DGCP_ADC_ACCESS_TOKEN=${GCP_ADC_ACCESS_TOKEN}
ENTRYPOINT ["/bin/sh", "-c", \
            "echo 'Starting the application...'; \
             ${APP_DIR}"]