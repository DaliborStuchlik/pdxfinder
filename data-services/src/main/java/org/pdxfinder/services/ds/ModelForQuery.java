package org.pdxfinder.services.ds;

/*
 * Created by csaba on 19/01/2018.
 */




/*

Patient/tumour

    cancer categories/classification – higher category  - by organ (breast, lung, colon), system (digestive system) or cell type (adenocarcinoma)
    cancer histology - granular
    type- met primary other
    age – bin?
    gender
    Patient treatment naïve, pretreated, unknown

if not naive – being more granular on treatment if possible
PDX model

    host strain
    mouse humanization
    site of graft - back, right flank
    type of graft - ortho or SC
    validation : yes/no, method

 */

/*
FACET OPTIONS:

patientAge
patientTreatmentStatus
patientGender

sampleOriginTissue
sampleSampleSite
sampleExtractionMethod
sampleClassification
sampleTumorType

modelImplantationSite
modelImplantationType
modelBackgroundStrain

*/


import java.util.List;
import java.util.stream.Collectors;

public class ModelForQuery {

    private Long modelId;
    private String externalId;

    private String datasource;

    private String patientAge;
    private String patientTreatmentStatus;
    private String patientGender;

    private String sampleOriginTissue;
    private String sampleSampleSite;
    private String sampleExtractionMethod;
    private String sampleClassification;
    private String sampleTumorType;

    private String modelImplantationSite;
    private String modelImplantationType;
    private String modelBackgroundStrain;

    private List<String> cancerSystem;
    private String cancerOrgan;
    private String cancerCellType;

    private String diagnosis;
    private String mappedOntologyTerm;
    private String treatmentHistory;
    private String dataAvailable;

    public ModelForQuery() {
    }


    public String getBy(SearchFacetName facet) {
        String s;
        switch (facet) {
            case datasource:
                s = datasource;
                break;
            case patient_age:
                s = patientAge;
                break;
            case patient_treatment_status:
                s = patientTreatmentStatus;
                break;
            case patient_gender:
                s = patientGender;
                break;
            case sample_origin_tissue:
                s = sampleOriginTissue;
                break;
            case sample_classification:
                s = sampleClassification;
                break;
            case sample_tumor_type:
                s = sampleTumorType;
                break;
            case model_implantation_site:
                s = modelImplantationSite;
                break;
            case model_implantation_type:
                s = modelImplantationType;
                break;
            case model_background_strain:
                s = modelBackgroundStrain;
                break;
            case organ:
                s = cancerOrgan;
                break;
            case system:
                // Pass back the list of top level ontology systems delimited by "::"
                s = cancerSystem.stream().collect(Collectors.joining("::"));
                break;
            case cell_type:
                s = cancerCellType;
                break;
            default:
                s = null;
                break;
        }
        return s;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientTreatmentStatus() {
        return patientTreatmentStatus;
    }

    public void setPatientTreatmentStatus(String patientTreatmentStatus) {
        this.patientTreatmentStatus = patientTreatmentStatus;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getSampleOriginTissue() {
        return sampleOriginTissue;
    }

    public void setSampleOriginTissue(String sampleOriginTissue) {
        this.sampleOriginTissue = sampleOriginTissue;
    }

    public String getSampleSampleSite() {
        return sampleSampleSite;
    }

    public void setSampleSampleSite(String sampleSampleSite) {
        this.sampleSampleSite = sampleSampleSite;
    }

    public String getSampleExtractionMethod() {
        return sampleExtractionMethod;
    }

    public void setSampleExtractionMethod(String sampleExtractionMethod) {
        this.sampleExtractionMethod = sampleExtractionMethod;
    }

    public String getSampleClassification() {
        return sampleClassification;
    }

    public void setSampleClassification(String sampleClassification) {
        this.sampleClassification = sampleClassification;
    }

    public String getSampleTumorType() {
        return sampleTumorType;
    }

    public void setSampleTumorType(String sampleTumorType) {
        this.sampleTumorType = sampleTumorType;
    }

    public String getModelImplantationSite() {
        return modelImplantationSite;
    }

    public void setModelImplantationSite(String modelImplantationSite) {
        this.modelImplantationSite = modelImplantationSite;
    }

    public String getModelImplantationType() {
        return modelImplantationType;
    }

    public void setModelImplantationType(String modelImplantationType) {
        this.modelImplantationType = modelImplantationType;
    }

    public String getModelBackgroundStrain() {
        return modelBackgroundStrain;
    }

    public void setModelBackgroundStrain(String modelBackgroundStrain) {
        this.modelBackgroundStrain = modelBackgroundStrain;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public List<String> getCancerSystem() {
        return cancerSystem;
    }

    public void setCancerSystem(List<String> cancerSystem) {
        this.cancerSystem = cancerSystem;
    }

    public String getCancerOrgan() {
        return cancerOrgan;
    }

    public void setCancerOrgan(String cancerOrgan) {
        this.cancerOrgan = cancerOrgan;
    }

    public String getCancerCellType() {
        return cancerCellType;
    }

    public void setCancerCellType(String cancerCellType) {
        this.cancerCellType = cancerCellType;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getMappedOntologyTerm() {
        return mappedOntologyTerm;
    }

    public void setMappedOntologyTerm(String mappedOntologyTerm) {
        this.mappedOntologyTerm = mappedOntologyTerm;
    }

    public String getTreatmentHistory() {
        return treatmentHistory;
    }

    public void setTreatmentHistory(String treatmentHistory) {
        this.treatmentHistory = treatmentHistory;
    }

    public String getDataAvailable() {
        return dataAvailable;
    }

    public void setDataAvailable(String dataAvailable) {
        this.dataAvailable = dataAvailable;
    }
}
