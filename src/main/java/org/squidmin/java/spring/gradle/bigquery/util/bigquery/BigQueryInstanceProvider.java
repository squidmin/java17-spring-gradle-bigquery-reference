package org.squidmin.java.spring.gradle.bigquery.util.bigquery;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import org.apache.commons.lang3.StringUtils;
import org.squidmin.java.spring.gradle.bigquery.logger.Logger;
import org.squidmin.java.spring.gradle.bigquery.logger.LoggerUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BigQueryInstanceProvider {

    public static BigQuery defaultInstance(String gcpSaKeyPath, String gcpAccessToken, String gcpProjectId) {
        File serviceAccountKey = readServiceAccountKeyFile(gcpSaKeyPath);
        Logger.log(String.format("GCP_SA_KEY_PATH == %s", StringUtils.isNotEmpty(gcpSaKeyPath) ? gcpSaKeyPath : ""), Logger.LogType.CYAN);
        Logger.log(String.format("GCP_ACCESS_TOKEN == %s", StringUtils.isNotEmpty(gcpAccessToken) ? gcpAccessToken.substring(0, 16) + "..." : ""), Logger.LogType.CYAN);

        BigQueryOptions.Builder bqOptionsBuilder = BigQueryOptions.newBuilder();
        boolean isBqJdkAuthenticatedUsingSaKeyFile = setServiceAccountCredentials(
            bqOptionsBuilder, gcpProjectId, serviceAccountKey
        );

        LoggerUtil.logSaKeyFileAuth(isBqJdkAuthenticatedUsingSaKeyFile);

        BigQuery bigQuery;
        if (!isBqJdkAuthenticatedUsingSaKeyFile && StringUtils.isNotEmpty(gcpAccessToken)) {
            Logger.log("Authenticated successfully using Application Default Credentials (ADC) access token.", Logger.LogType.INFO);
            bigQuery = bqOptionsBuilder.setCredentials(
                GoogleCredentials.newBuilder()
                    .setAccessToken(AccessToken.newBuilder().setTokenValue(gcpAccessToken).build())
                    .build()
            ).build().getService();
        } else {
            Logger.log("Was not able to authenticate using Application Default Credentials (ADC) access token.", Logger.LogType.INFO);
            bigQuery = bqOptionsBuilder.build().getService();
        }
        return bigQuery;
    }

    private static File readServiceAccountKeyFile(String gcpSaKeyPath) {
        return new File(StringUtils.isNotEmpty(gcpSaKeyPath) ? gcpSaKeyPath : "");
    }

    private static boolean setServiceAccountCredentials(BigQueryOptions.Builder bqOptionsBuilder, String gcpProjectId, File serviceAccountKey) {
        bqOptionsBuilder.setProjectId(gcpProjectId).setLocation("us");
        GoogleCredentials credentials;
        boolean isBqJdkAuthenticatedUsingSaKeyFile;
        try (FileInputStream stream = new FileInputStream(serviceAccountKey)) {
            credentials = ServiceAccountCredentials.fromStream(stream);
            Logger.log("BQ JDK: Setting service account credentials (GOOGLE_APPLICATION_CREDENTIALS) to BQ options.", Logger.LogType.CYAN);
            bqOptionsBuilder.setCredentials(credentials);
            isBqJdkAuthenticatedUsingSaKeyFile = true;
        } catch (IOException e) {
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
            if (e.getMessage().contains("'type' value 'authorized_user' not recognized. Expecting 'service_account'")) {
                Logger.log("If you're trying to use Application Default Credentials (ADC), use the command:", Logger.LogType.ERROR);
                Logger.log("    gcloud auth application-default print-access-token", Logger.LogType.ERROR);
                Logger.log("to generate a GCP access token and set the output of the command to the \"GCP_ACCESS_TOKEN\" environment variable.", Logger.LogType.ERROR);
            }
            isBqJdkAuthenticatedUsingSaKeyFile = false;
        }
        return isBqJdkAuthenticatedUsingSaKeyFile;
    }

}
