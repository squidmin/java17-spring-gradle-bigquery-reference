package org.squidmin.java.spring.gradle.bigquery.service;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.ImpersonatedCredentials;
import com.google.cloud.storage.*;
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
    private final Storage storage;

    public GcsService(GcsConfig gcsConfig) {
        this.gcsConfig = gcsConfig;
        this.storage = gcsConfig.getStorage();
    }

    public Bucket createBucket(String bucketName) {
        if (storage.get(bucketName) != null) {
            throw new IllegalArgumentException("Bucket " + bucketName + " already exists.");
        }
        return storage.create(BucketInfo.of(bucketName));
    }

    public boolean bucketExists(String bucketName) {
        Bucket bucket = storage.get(bucketName);
        return bucket != null;
    }

    public URL upload(List<ExampleResponseItem> responseItems) {
        List<String> csvRows = new ArrayList<>();
        csvRows.add(ExampleResponse.getCsvHeaders());
        for (ExampleResponseItem responseItem : responseItems) {
            csvRows.add(getRow(responseItem));
        }
        String csvContent = String.join("\n", csvRows);
        BlobInfo blobInfo = buildBlobInfo();
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
        return String.join(
            ",",
            responseItem.getId(),
            responseItem.getCreationTimestamp(),
            responseItem.getLastUpdateTimestamp(),
            responseItem.getColumnA(),
            responseItem.getColumnB()
        );
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
