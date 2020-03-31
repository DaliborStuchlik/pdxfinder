package org.pdxfinder.services.dto;

import java.util.HashMap;
import java.util.Map;

public class DSPProjectRef {

    private String alias;
    private String team;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public DSPProjectRef(String alias, String team){
        this.alias = alias;
        this.team = team;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }
}
