package org.pdxfinder.services.dto;


public class DSPProjectDTO {

    private String alias;
    private String title;
    private String description;
    private String releaseDate;

    public DSPProjectDTO(String alias, String title, String description, String releaseDate){
        this.alias = alias;
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
