package org.pdxfinder.dsp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pdxfinder.services.dto.DSPProjectDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;


import java.io.IOException;


public class DspHttpRequests {

    Logger log = LoggerFactory.getLogger(DspHttpRequests.class);

    DspHttpClientInterface httpClient = new DspHttpClient();

    public void createProject(String token, String submissionId, DSPProjectDTO project) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String projectJson = mapper.writeValueAsString(project);
        String postProjectUrl = "https://submission-test.ebi.ac.uk/api/submissions/" + submissionId + "/contents/projects";
        ResponseEntity<String> response = httpClient.httpPostJsonWithAuth(postProjectUrl, token, projectJson);
        assert response.getStatusCodeValue() == 201;
    }

    public String getNewSubmissionId(String token, String team) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String postSubmissionURI = "https://submission-test.ebi.ac.uk/api/teams/" + team + "/submissions";
        String emptyJson = "{}";
        String response = httpClient.httpPostJsonWithAuth(postSubmissionURI,token, emptyJson).getBody();
        return mapper.readTree(response).get("id").toString().replace("\"","");
    }

    public String getToken() {
        String url = "https://explore.api.aai.ebi.ac.uk/auth?ttl=180";
        String authStr = "afollette2";
        String password = "@10CofeeCups";
        ResponseEntity<String> response =httpClient.httpBasicAuth(url, authStr, password);
        assert response.getStatusCodeValue() == 200;
        return response.getBody();
    }

    public String getTeamName(String token) throws IOException {
        String responseBody = httpClient.httpGetWithAuth("https://submission-test.ebi.ac.uk/api/user/teams", token).getBody();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(responseBody)
                .get("_embedded")
                .get("teams")
                .get(0)
                .get("name")
                .toString()
                .replace("\"","");
    }

    public void setHttpClient(DspHttpClientInterface httpClient) {
        this.httpClient = httpClient;
    }

}
