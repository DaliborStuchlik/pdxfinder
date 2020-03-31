package org.pdxfinder.dsp;

import io.tus.java.client.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DspUploadClient implements DspUploadInterface {

    TusClient client = new TusClient();

    public void uploadToDsp(String fileUrl, String token, String submissionId) throws IOException, ProtocolException {
        String url = "https://submission-test.ebi.ac.uk/files/";
        Map<String, String> headers = new HashMap<>();
        headers.put("jwtToken",token);
        headers.put("submissionID",submissionId);
        headers.put("name", new File(fileUrl).getName());
        uploadFile(url, fileUrl, headers);
    }

    public void uploadFile(String url, String fileUrl, Map<String,String> headers) throws IOException, ProtocolException {

        client.setUploadCreationURL(new URL(url));
        client.enableResuming(new TusURLMemoryStore());

        File file = new File(fileUrl);
        final TusUpload upload = new TusUpload(file);
        upload.setMetadata(headers);

        System.out.println("Starting upload...");
        TusExecutor executor = new TusExecutor() {
            @Override
            protected void makeAttempt() throws ProtocolException, IOException {
                TusUploader uploader = client.resumeOrCreateUpload(upload);
                uploader.setChunkSize(1024);

                int i = 0;
                
                do {
                    i++;
                    if (i % 100000 != 0) {
                        long totalBytes = upload.getSize();
                        long bytesUploaded = uploader.getOffset();
                        double progress = (double) bytesUploaded / totalBytes * 100;

                        System.out.printf("Upload at %06.2f%%.\n", progress);
                    }
                } while (uploader.uploadChunk() > -1);

                uploader.finish();

                System.out.println("Upload finished.");
                System.out.format("Upload available at: %s", uploader.getUploadURL().toString());
            }
        };
        executor.makeAttempts();
    }
}
