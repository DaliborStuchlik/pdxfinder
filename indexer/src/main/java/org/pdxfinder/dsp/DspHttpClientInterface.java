package org.pdxfinder.dsp;

import org.springframework.http.ResponseEntity;

public interface DspHttpClientInterface  {

    public ResponseEntity<String> httpGetWithAuth(String url, String token);
    public ResponseEntity<String> httpPostJsonWithAuth(String url, String token, String json);
    public ResponseEntity<String> httpBasicAuth(String url, String username, String password);

}
