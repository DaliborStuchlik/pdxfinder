package org.pdxfinder.dsp;

import io.tus.java.client.ProtocolException;

import java.io.IOException;

public interface DspUploadInterface {

    public void uploadToDsp(String fileUrl, String token, String submissionId) throws IOException, ProtocolException;


}
