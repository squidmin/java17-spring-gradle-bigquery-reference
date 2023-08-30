package org.squidmin.java.spring.gradle.bigquery.service;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.ImpersonatedCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.squidmin.java.spring.gradle.bigquery.config.GcsConfig;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponse;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponseItem;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class GcsService {

    private final GcsConfig gcsConfig;

    public GcsService(GcsConfig gcsConfig) {
        this.gcsConfig = gcsConfig;
    }

    public URL upload(List<ExampleResponseItem> responseItems) {
        List<String> csvRows = new ArrayList<>();
        csvRows.add(ExampleResponse.getCsvHeaders());
        for (ExampleResponseItem responseItem : responseItems) {
            csvRows.add(getRow(responseItem));
        }
        String csvContent = String.join("\n", csvRows);

        BlobInfo blobInfo = buildBlobInfo();

        Storage storage = gcsConfig.getStorage();
        storage.create(blobInfo, csvContent.getBytes());

        URL signedUrl = storage.signUrl(blobInfo, 5, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature());

        return signedUrl;
    }

    private void refreshImpersonatedCredentials(String accessToken) {
        gcsConfig.setStorage(
            StorageOptions.newBuilder()
                .setCredentials(
                    ImpersonatedCredentials.create(
                        AccessToken.newBuilder()
                            .setTokenValue(accessToken)
                            .build()
                    )
                )
                .build().getService()
        );
    }

    private String getRow(ExampleResponseItem responseItem) {
        String row = String.join(
            ",",
            responseItem.getId(),
            responseItem.getCreationTimestamp(),
            responseItem.getLastUpdateTimestamp(),
            responseItem.getColumnA(),
            responseItem.getColumnB()
        );
        return row;
    }

    private BlobInfo buildBlobInfo() {
        return BlobInfo.newBuilder(
                BlobId.of(
                    gcsConfig.getGcsBucketName(),
                    gcsConfig.getGcsFilename().concat(".csv")
                )
            )
            .setContentType("text/csv")
            .build();
    }

}
