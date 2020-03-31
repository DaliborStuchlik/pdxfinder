package org.pdxfinder.services.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

public class DSPStudyDTO {

    private String alias;
    private String title;
    private String description;
    private DSPProjectRef projectRef;

    public DSPProjectRef getProjectRef() {
        return projectRef;
    }

    public void setProjectRef(DSPProjectRef project_reference) {
        this.projectRef = project_reference;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
}
