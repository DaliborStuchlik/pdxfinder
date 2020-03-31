package org.pdxfinder.services.dto;

import java.util.List;
import java.util.Map;

public class DSPSampleDTO {

    private String taxid;
    private String scientificname;
    private String commonname;
    private Map<String, Object> attributes;
    private String sampletitle;
    private String sampledescription;
    private String sampleorigin;
    private String sampleTaxonName;
    private String sampleMaterial;
    private String engraftedTumorSamplePassage;
    private String engraftedTumorCollectionSite;
    private String patientTumorSiteOfCollection;
    private String patientTumorType;
    private String sampleUniqueId;
    private String engraftmentHostStrainName;
    private String patientAgeAtCollectionOfTumor;
    private String patientTumorDiagnosisAtTimeOfCollection;
    private String patientTumorPrimarySite;
    private String wasThePdxModelHumanised;
    private String patientSex;


    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getTaxid() {
        return taxid;
    }

    public void setTaxid(String taxid) {
        this.taxid = taxid;
    }

    public String getScientificname() {
        return scientificname;
    }

    public void setScientificname(String scientificname) {
        this.scientificname = scientificname;
    }

    public String getCommonname() {
        return commonname;
    }

    public void setCommonname(String commonname) {
        this.commonname = commonname;
    }

    public String getSampletitle() {
        return sampletitle;
    }

    public void setSampletitle(String sampletitle) {
        this.sampletitle = sampletitle;
    }

    public String getSampledescription() {
        return sampledescription;
    }

    public void setSampledescription(String sampledescription) {
        this.sampledescription = sampledescription;
    }

    public String getSampleorigin() {
        return sampleorigin;
    }

    public void setSampleorigin(String sampleorigin) {
        this.sampleorigin = sampleorigin;
    }

    public String getSampleTaxonName() {
        return sampleTaxonName;
    }

    public void setSampleTaxonName(String sampleTaxonName) {
        this.sampleTaxonName = sampleTaxonName;
    }

    public String getSampleMaterial() {
        return sampleMaterial;
    }

    public void setSampleMaterial(String sampleMaterial) {
        this.sampleMaterial = sampleMaterial;
    }

    public String getEngraftedTumorSamplePassage() {
        return engraftedTumorSamplePassage;
    }

    public void setEngraftedTumorSamplePassage(String engraftedTumorSamplePassage) {
        this.engraftedTumorSamplePassage = engraftedTumorSamplePassage;
    }

    public String getEngraftedTumorCollectionSite() {
        return engraftedTumorCollectionSite;
    }

    public void setEngraftedTumorCollectionSite(String engraftedTumorCollectionSite) {
        this.engraftedTumorCollectionSite = engraftedTumorCollectionSite;
    }

    public String getPatientTumorSiteOfCollection() {
        return patientTumorSiteOfCollection;
    }

    public void setPatientTumorSiteOfCollection(String patientTumorSiteOfCollection) {
        this.patientTumorSiteOfCollection = patientTumorSiteOfCollection;
    }

    public String getPatientTumorType() {
        return patientTumorType;
    }

    public void setPatientTumorType(String patientTumorType) {
        this.patientTumorType = patientTumorType;
    }

    public String getSampleUniqueId() {
        return sampleUniqueId;
    }

    public void setSampleUniqueId(String sampleUniqueId) {
        this.sampleUniqueId = sampleUniqueId;
    }

    public String getEngraftmentHostStrainName() {
        return engraftmentHostStrainName;
    }

    public void setEngraftmentHostStrainName(String engraftmentHostStrainName) {
        this.engraftmentHostStrainName = engraftmentHostStrainName;
    }

    public String getPatientAgeAtCollectionOfTumor() {
        return patientAgeAtCollectionOfTumor;
    }

    public void setPatientAgeAtCollectionOfTumor(String patientAgeAtCollectionOfTumor) {
        this.patientAgeAtCollectionOfTumor = patientAgeAtCollectionOfTumor;
    }

    public String getPatientTumorDiagnosisAtTimeOfCollection() {
        return patientTumorDiagnosisAtTimeOfCollection;
    }

    public void setPatientTumorDiagnosisAtTimeOfCollection(String patientTumorDiagnosisAtTimeOfCollection) {
        this.patientTumorDiagnosisAtTimeOfCollection = patientTumorDiagnosisAtTimeOfCollection;
    }

    public String getPatientTumorPrimarySite() {
        return patientTumorPrimarySite;
    }

    public void setPatientTumorPrimarySite(String patientTumorPrimarySite) {
        this.patientTumorPrimarySite = patientTumorPrimarySite;
    }

    public String getWasThePdxModelHumanised() {
        return wasThePdxModelHumanised;
    }

    public void setWasThePdxModelHumanised(String wasThePdxModelHumanised) {
        this.wasThePdxModelHumanised = wasThePdxModelHumanised;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public void setPatientSex(String patientSex) {
        this.patientSex = patientSex;
    }
}
