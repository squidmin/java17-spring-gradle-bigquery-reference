# ---- Build Stage ----
FROM gradle:8.3-jdk17 AS build

# Set a volume point for temp to get a performance improvement
#VOLUME /tmp

### Build arguments ###
ARG JAR_FILE=build/libs/*.jar
ARG APP_DIR=/usr/local/app
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

# Set working directory
WORKDIR /app

# Copy the source code
COPY . .

# Build the application
RUN gradle clean bootJar

# ---- Run Stage ----
FROM openjdk:17-jdk-slim

# Set application port
EXPOSE 8080

# Set working directory
WORKDIR /app

# Copy the executable jar from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Run the application
CMD ["java", "-jar", "app.jar"]
