package org.pdxfinder.dsp;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

public class DspHttpClient implements DspHttpClientInterface{

    public ResponseEntity<String> httpGetWithAuth(String url, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity request = new HttpEntity(headers);
        logRequest(request);
        ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.GET, request, String.class);
        return response;
    }

    public ResponseEntity<String> httpPostJsonWithAuth(String url, String token, String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request = new HttpEntity(json, headers);
        logRequest(request);
        return new RestTemplate().postForEntity(url, request, String.class);
    }

    public ResponseEntity<String> httpBasicAuth(String url, String username, String password){
        String authStr =  username + ":" + password;
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        HttpEntity request = new HttpEntity(headers);
        logRequest(request);
        return new RestTemplate().exchange(url, HttpMethod.GET, request, String.class);
    }

    private void logRequest(HttpEntity request){
        System.out.println("Headers: " + request.getHeaders().toString() + "\n");
        System.out.println("Body : " + request.getBody() + "\n");
    }
}
