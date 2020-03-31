package org.pdxfinder.dsp;

import io.tus.java.client.ProtocolException;
import org.pdxfinder.services.dto.DSPAssayDTO;
import org.pdxfinder.services.dto.DSPSampleDTO;
import org.pdxfinder.services.dto.DSPStudyDTO;
import org.pdxfinder.services.dto.DSPProjectDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;


public class SubmitToUSI {

    Logger log = LoggerFactory.getLogger(SubmitToUSI.class);

    DspHttpRequests requests = new DspHttpRequests();
    DspUploadInterface uploadClient = new DspUploadClient();

    private static String token;
    private static String team;


    private String submissionId;
    private String projectName = "PDX-ENA_client";

    public void submit(String sampleId, String file1, String file2) throws IOException, ProtocolException {

        readInMetadata();
        log.info("Submission Id : " + submissionId);
        DSPProjectDTO project = createProjectDto(file1);
        DSPStudyDTO study = createStudyDto();

        initRestSubmission();
        submissionId = requests.getNewSubmissionId(token, team);
        requests.createProject(token, submissionId, project);

        submitFASTQs(file1, file2);
    }


    private void readInMetadata() {
    }

    private void submitFASTQs(String file1, String file2) throws IOException, ProtocolException {

        tusUploadFiles(file1, file2);

        DSPSampleDTO sample = createSampleDto();
        DSPAssayDTO assay = createAssayDto();
    }

    private DSPProjectDTO createProjectDto(String file1){
        Calendar cal = Calendar.getInstance();
        String yearMonthDay = String.format("%d-%02d-%02d",cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
        File file = new File(file1);
        String fileName1 = file.getName();
        String description = "Submission of pair " + file1 + " from PDX-Finder on behalf of PDMR";
        return new DSPProjectDTO(fileName1, projectName, description, yearMonthDay );
    }

    private DSPStudyDTO createStudyDto(){
        return new DSPStudyDTO();
    }

    private DSPAssayDTO createAssayDto(){
        return new DSPAssayDTO();
    }

    private DSPSampleDTO createSampleDto() {
        return new DSPSampleDTO();
    }

    public void initRestSubmission() throws IOException {
        token = requests.getToken();
        log.info("Token : " + token);
        team = requests.getTeamName(token);
        log.info("Team : " + team);
    }

    public void tusUploadFiles(String file1, String file2) throws IOException, ProtocolException {
        uploadClient.uploadToDsp(file1,token, submissionId);
        uploadClient.uploadToDsp(file2,token, submissionId);
    }
}



