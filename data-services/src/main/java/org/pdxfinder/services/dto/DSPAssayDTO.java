package org.pdxfinder.services.dto;

import java.util.List;
import java.util.Map;

public class DSPAssayDTO {

    private String sample_alias;
    private String title;
    private String description;
    private Map<String,Object> attributes;
    private Map<String, String> studyRef;
    private List<Object> sampleRef;

    public String getSample_alias() {
        return sample_alias;
    }

    public void setSample_alias(String sample_alias) {
        this.sample_alias = sample_alias;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String,String>  getStudyRef() {
        return studyRef;
    }

    public void setStudyRef(Map<String,String> studyRef) {
        this.studyRef = studyRef;
    }

    public List<Object> getSampleRef() {
        return sampleRef;
    }

    public void setSampleRef(List<Object> sampleRef) {
        this.sampleRef = sampleRef;
    }
}
