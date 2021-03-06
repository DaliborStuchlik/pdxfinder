package org.pdxfinder.services.highchart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/*
 * Created by abayomi on 19/06/2019.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "categories"
})
public class XAxis {


    private List<String> categories;
    private Boolean crosshair;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public XAxis() {
    }

    public XAxis(List<String> categories) {
        this.categories = categories;
    }

    public XAxis(List<String> categories, Boolean crosshair) {
        this.categories = categories;
        this.crosshair = crosshair;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Boolean getCrosshair() {
        return crosshair;
    }

    public void setCrosshair(Boolean crosshair) {
        this.crosshair = crosshair;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}