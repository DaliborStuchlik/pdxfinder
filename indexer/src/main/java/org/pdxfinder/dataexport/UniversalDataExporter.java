package org.pdxfinder.dataexport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.pdxfinder.graph.dao.*;
import org.pdxfinder.services.DataImportService;
import org.pdxfinder.services.UtilityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/*
 * Created by csaba on 02/10/2019.
 */
@Service
public class UniversalDataExporter {

    private static final Logger log = LoggerFactory.getLogger(UniversalDataExporter.class);

    protected UtilityService utilityService;
    protected DataImportService dataImportService;

    protected String templateDir;

    private List<List<String>> patientSheetDataExport;
    private List<List<String>> patientTumorSheetDataExport;
    private List<List<String>> patientTreatmentSheetDataExport;
    private List<List<String>> pdxModelSheetDataExport;
    private List<List<String>> pdxModelValidationSheetDataExport;
    private List<List<String>> samplePlatformDescriptionSheetDataExport;
    private List<List<String>> sharingAndContactSheetDataExport;
    private List<List<String>> loaderRelatedDataSheetDataExport;
    private List<List<String>> drugDosingSheetDataExport;

    private List<List<String>> mutationSheetDataExport;
    private List<List<String>> cnaSheetDataExport;
    private List<List<String>> cytogeneticsSheetDataExport;
    private List<List<String>> expressionSheetDataExport;

    private Group ds;

    private static String notSpecified = "Not Specified";
    private static String patientOrigin = "patient";

    @Autowired
    public UniversalDataExporter(DataImportService dataImportService, UtilityService utilityService) {

        this.dataImportService = dataImportService;
        this.utilityService = utilityService;

        patientSheetDataExport = new ArrayList<>();
        patientTumorSheetDataExport = new ArrayList<>();
        pdxModelSheetDataExport = new ArrayList<>();
        pdxModelValidationSheetDataExport = new ArrayList<>();
        sharingAndContactSheetDataExport = new ArrayList<>();
        loaderRelatedDataSheetDataExport = new ArrayList<>();

        samplePlatformDescriptionSheetDataExport = new ArrayList<>();

        mutationSheetDataExport = new ArrayList<>();
        cnaSheetDataExport = new ArrayList<>();
        cytogeneticsSheetDataExport = new ArrayList<>();
        expressionSheetDataExport = new ArrayList<>();
    }

    public Group getDs() {
        return ds;
    }

    public void setDs(Group ds) {
        this.ds = ds;
    }

    public String getTemplateDir() {
        return templateDir;
    }

    public void setTemplateDir(String templateDir) {
        this.templateDir = templateDir;
    }

    public void init(String templateDir, Group ds){

        this.templateDir = templateDir;
        this.ds = ds;

        initPatientData();
        initPatientTumorAtCollection();
        initPdxModelDetails();
        initPdxModelValidations();
        initSharingAndContact();
        initLoaderRelatedData();

        initSamplePlatformDescription();

        initOmicData();
    }

    public void export(String exportDir) throws IOException {

        XSSFWorkbook metadataTemplate = getWorkbook(templateDir+"/metadata_template.xlsx");
        XSSFWorkbook samplePlatformTemplate = getWorkbook(templateDir+"/sampleplatform_template.xlsx");
        XSSFWorkbook mutationTemplate = getWorkbook(templateDir+"/mutation_template.xlsx");
        XSSFWorkbook cnaTemplate = getWorkbook(templateDir+"/cna_template.xlsx");
        XSSFWorkbook cytoTemplate = getWorkbook(templateDir + "/cytogenetics_template.xlsx");
        XSSFWorkbook exprTemplate = getWorkbook(templateDir + "/expression_template.xlsx");

        if (metadataTemplate != null && noMetaDataSheetsAreNull()) {
            updateSheetWithData(metadataTemplate.getSheetAt(1), patientSheetDataExport, 6, 2);
            updateSheetWithData(metadataTemplate.getSheetAt(2), patientTumorSheetDataExport, 6, 2);
            updateSheetWithData(metadataTemplate.getSheetAt(3), pdxModelSheetDataExport, 6, 2);
            updateSheetWithData(metadataTemplate.getSheetAt(4), pdxModelValidationSheetDataExport, 6, 2);
            updateSheetWithData(metadataTemplate.getSheetAt(5), sharingAndContactSheetDataExport, 6, 2);
            updateSheetWithData(metadataTemplate.getSheetAt(6), loaderRelatedDataSheetDataExport, 6, 2);
        }

        if (samplePlatformTemplate != null && samplePlatformDescriptionSheetDataExport != null) {
            updateSheetWithData(samplePlatformTemplate.getSheetAt(0), samplePlatformDescriptionSheetDataExport, 6, 1);
        }

        Path exportProviderDir = Paths.get(exportDir + "/" + ds.getAbbreviation());
        Files.createDirectories(exportProviderDir);

        try {
            if(metadataTemplate != null && noMetaDataSheetsAreNull()) {
                writeFileFromWorkbook(metadataTemplate, exportProviderDir + "/metadata.xlsx");
            }
            if(samplePlatformTemplate != null && samplePlatformDescriptionSheetDataExport != null) {
                writeFileFromWorkbook(samplePlatformTemplate, exportProviderDir + "/sampleplatform.xlsx");
            }
            writeOmicFileFromWorkbook(mutationTemplate, mutationSheetDataExport, exportProviderDir + "/mut/", "_mut.tsv");
            writeOmicFileFromWorkbook(cnaTemplate, cnaSheetDataExport, exportProviderDir + "/cna/", "_cna.tsv" );
            writeOmicFileFromWorkbook(cytoTemplate, cytogeneticsSheetDataExport, exportProviderDir + "/cyto/", "_cyto.tsv" );
            writeOmicFileFromWorkbook(exprTemplate, expressionSheetDataExport, exportProviderDir + "/expr/", "_expr.tsv");
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    private boolean noMetaDataSheetsAreNull(){
        return (patientSheetDataExport != null && patientTumorSheetDataExport != null
                && pdxModelSheetDataExport != null && pdxModelValidationSheetDataExport != null
                && sharingAndContactSheetDataExport != null && loaderRelatedDataSheetDataExport != null);
    }

    private void writeFileFromWorkbook(XSSFWorkbook dataWorkbook, String fileLocation) throws IOException {
        if (dataWorkbook != null) {
            FileOutputStream fileOut = new FileOutputStream(fileLocation);
            dataWorkbook.write(fileOut);
            fileOut.close();
        }
    }

    private void writeOmicFileFromWorkbook(XSSFWorkbook omicWorkbook,List<List<String>> exportSheet, String fileLocation, String suffix ) throws IOException {
        if (omicWorkbook != null && exportSheet != null && !exportSheet.isEmpty()) {
            if (!Paths.get(fileLocation).toFile().exists()) {
                Files.createDirectory(Paths.get(fileLocation));
            }
            String exportURI = fileLocation +  ds.getAbbreviation() + suffix;
            readSheetAndWriteOmicTsvFile(omicWorkbook.getSheetAt(0),exportSheet, exportURI);
        }
    }

    public void initPatientData() {

        if (ds == null) return;

        List<Patient> patients = dataImportService.findPatientsByGroup(ds);

        for (Patient patient : patients) {

            List<String> dataRow = new ArrayList<>();

            String patientId = patient.getExternalId();
            String sex = patient.getSex();
            String cancerHistory = patient.getCancerRelevantHistory();
            String ethnicity = patient.getEthnicity();
            String ethnicityAssessment = patient.getEthnicityAssessment();
            String firstDiagnosis = patient.getFirstDiagnosis();
            String ageAtFirstDiagnosis = patient.getAgeAtFirstDiagnosis();

            dataRow.add(patientId);
            dataRow.add(sex);
            dataRow.add(cancerHistory);
            dataRow.add(ethnicity);
            dataRow.add(ethnicityAssessment);
            dataRow.add(firstDiagnosis);
            dataRow.add(ageAtFirstDiagnosis);

            patientSheetDataExport.add(dataRow);
        }
    }

    public void initPatientTumorAtCollection(){

        if (ds == null) return;

        List<Patient> patients = dataImportService.findPatientTumorAtCollectionDataByDS(ds);

        for(Patient patient : patients){

            String patientId = patient.getExternalId();

            for(PatientSnapshot patientSnapshot : patient.getSnapshots()){

                for(Sample sample : patientSnapshot.getSamples()){

                    List<String> dataRow = new ArrayList<>();

                    String sampleId = sample.getSourceSampleId();
                    String collectionDate = patientSnapshot.getDateAtCollection();
                    String collectionEvent = patientSnapshot.getCollectionEvent();
                    String elapsedTime = patientSnapshot.getElapsedTime();
                    String ageAtCollection = patientSnapshot.getAgeAtCollection();
                    String diagnosis = sample.getDiagnosis();
                    String tumorType = sample.getType().getName();
                    String primarySite = sample.getOriginTissue().getName();
                    String collectionSite = sample.getSampleSite().getName();
                    String stage = sample.getStage();
                    String stageClassification = sample.getStageClassification();
                    String grade = sample.getGrade();
                    String gradeClassification = sample.getGradeClassification();
                    String virologyStatus = patientSnapshot.getVirologyStatus();
                    String isPatientTreatmentInfoAvailable = "";
                    String treatmentNaive = patientSnapshot.getTreatmentNaive();
                    String isPatientTreated = "";
                    String wasPatientTreated = "";
                    String modelId = "";
                    try {
                        modelId = dataImportService.findModelBySample(sample).getSourcePdxId();
                    }
                    catch (Exception e){
                        log.error("Error getting model Id from sample: {}", sampleId);
                    }

                    dataRow.add(patientId);
                    dataRow.add(sampleId);
                    dataRow.add(collectionDate);
                    dataRow.add(collectionEvent);
                    dataRow.add(elapsedTime);
                    dataRow.add(ageAtCollection);
                    dataRow.add(diagnosis);
                    dataRow.add(tumorType);
                    dataRow.add(primarySite);
                    dataRow.add(collectionSite);
                    dataRow.add(stage);
                    dataRow.add(stageClassification);
                    dataRow.add(grade);
                    dataRow.add(gradeClassification);
                    dataRow.add(virologyStatus);
                    dataRow.add(isPatientTreatmentInfoAvailable);
                    dataRow.add(treatmentNaive);
                    dataRow.add(isPatientTreated);
                    dataRow.add(wasPatientTreated);
                    dataRow.add(modelId);

                    patientTumorSheetDataExport.add(dataRow);
                }
            }
        }

    }

    public void initPdxModelDetails(){

        if (ds == null) return;

        List<ModelCreation> models = dataImportService.findModelsWithSpecimensAndQAByDS(ds.getAbbreviation());

        for(ModelCreation model : models){

            Map<String, ModelDetails> specimenMap = new HashMap<>();

            for(Specimen specimen : model.getSpecimens()){

                String passage = specimen.getPassage();

                String engraftmentSite = notSpecified;

                if(specimen.getEngraftmentSite() != null ){
                    engraftmentSite = specimen.getEngraftmentSite().getName();
                }

                String engraftmentType = notSpecified;

                if(specimen.getEngraftmentType() != null){
                   engraftmentType = specimen.getEngraftmentType().getName();
                }

                String engraftmentMaterial = notSpecified;
                String engraftmentMaterialStatus = notSpecified;

                if(specimen.getEngraftmentMaterial() != null){
                    engraftmentMaterial = specimen.getEngraftmentMaterial().getName();
                    engraftmentMaterialStatus = specimen.getEngraftmentMaterial().getState();
                }

                addEntryToSpecimenMap(
                    specimenMap,
                    specimen.getHostStrain(),
                    engraftmentSite,
                    engraftmentType,
                    engraftmentMaterial,
                    engraftmentMaterialStatus,
                    passage);
            }

            insertModelSheetDataFromSpecimenMap(specimenMap, model);

        }
    }

    public void initPdxModelValidations(){

        if (ds == null) return;

        List<ModelCreation> models = dataImportService.findModelsWithSpecimensAndQAByDS(ds.getAbbreviation());

        for(ModelCreation model : models){

            if(model != null && model.getQualityAssurance() != null) {

                String modelId = model.getSourcePdxId();

                for (QualityAssurance qa : model.getQualityAssurance()) {

                    List<String> dataRow = new ArrayList<>();

                    String validationTechnique = qa.getTechnology();
                    String validationDescription = qa.getDescription();
                    String passages = qa.getPassages();
                    String nomenclature = qa.getValidationHostStrain();

                    dataRow.add(modelId);
                    dataRow.add(validationTechnique);
                    dataRow.add(validationDescription);
                    dataRow.add(passages);
                    dataRow.add(nomenclature);

                    pdxModelValidationSheetDataExport.add(dataRow);
                }
            }
        }
    }

    public void initSharingAndContact(){

        if (ds == null) return;

        List<ModelCreation> models = dataImportService.findModelsWithSharingAndContactByDS(ds.getAbbreviation());

        for(ModelCreation model : models){

            LinkedHashMap<String, String> sharingAndContactRow = new LinkedHashMap<>();

            sharingAndContactRow.put("modelId", model.getSourcePdxId());

            Group providerGroup = getGroupByType(model, "Provider");
            Group accessGroup = getGroupByType(model, "Accessibility");
            Group projectGroup = getGroupByType(model, "Project");

            getGroupData(sharingAndContactRow, providerGroup, accessGroup, projectGroup);

            getExternalUrlData(sharingAndContactRow, model.getExternalUrls());

            insertSharingAndContactDataForModel(sharingAndContactRow);

        }
    }

    public void initLoaderRelatedData(){

        if (ds == null) return;

        List<String> dataRow = new ArrayList<>();
        dataRow.add(ds.getName());
        dataRow.add(ds.getAbbreviation());
        dataRow.add(ds.getUrl());

        loaderRelatedDataSheetDataExport.add(dataRow);

    }

    public void initSamplePlatformDescription(){

        if (ds == null) return;

        List<ModelCreation> models = dataImportService.findModelXenograftPlatformSampleByDS(ds.getAbbreviation());

        for(ModelCreation model : models){

            addPatientMolcharDataToSamplePlatform(model);
            addXenoMolcharDataToSamplePlatform(model);
        }

    }

    public void initOmicData(){
    initGenomicData(mutationSheetDataExport, "mutation");
    initGenomicData(cnaSheetDataExport, "copy number alteration");
    initGenomicData(cytogeneticsSheetDataExport, "cytogenetics");
    initGenomicData(expressionSheetDataExport, "expression");
   }


    private void initGenomicData(List<List<String>> sheetData, String molcharType){

        //TODO, these queries could be optimized to reduce db interactions
        List<ModelCreation> models = dataImportService.findModelsWithSharingAndContactByDS(ds.getAbbreviation());

        for(ModelCreation m: models){
            ModelCreation model = dataImportService.findModelWithMolecularDataByDSAndIdAndMolcharType(
                  ds.getAbbreviation(),
                  m.getSourcePdxId(),
                  molcharType);

            if(model != null){

                String modelId = model.getSourcePdxId();
                log.info("Exporting data for {}", modelId);

                initPatientGenomicData(model, sheetData);
                initXenoGenomicData(model, sheetData);
            }
        }
    }

    private void insertOmicDataToSheet(
        ModelCreation model,
        String sampleId,
        String sampleOrigin,
        String molcharType,
        Specimen specimen,
        MolecularCharacterization mc,
        List<List<String>> sheetData){

        for(MarkerAssociation ma: mc.getMarkerAssociations()){
            List<MolecularData> molecularData;
            try{
                molecularData = ma.decodeMolecularData();
            }
            catch(Exception e){
                log.error("No molecular data in Marker Association on Sample {}", sampleId);
                molecularData = new ArrayList<>();
            }
            for(MolecularData md : molecularData){
                List<String> rowData = new ArrayList<>();
                rowData.add(model.getSourcePdxId());
                rowData.add(sampleId);
                rowData.add(sampleOrigin);

                if(sampleOrigin.equals(patientOrigin)){
                    //no passage, host strain for patient samples
                    rowData.add("");
                    rowData.add("");
                }
                else{

                    rowData.add(specimen.getPassage());

                    if(specimen.getHostStrain() != null && specimen.getHostStrain().getSymbol() != null){
                        rowData.add(specimen.getHostStrain().getSymbol());
                    }
                    else{
                        rowData.add("");
                    }
                }

                switch (molcharType) {
                    case "mutation":
                        rowData.add(md.getMarker());
                        rowData.add(md.getAminoAcidChange());
                        rowData.add(md.getBiotype());
                        rowData.add(md.getCodingSequenceChange());
                        rowData.add(md.getVariantClass());
                        rowData.add(md.getNucleotideChange());
                        rowData.add(md.getCodonChange());
                        rowData.add(md.getAminoAcidChange());
                        rowData.add(md.getConsequence());
                        rowData.add(md.getReadDepth());
                        rowData.add(md.getAlleleFrequency());
                        rowData.add(md.getChromosome());
                        rowData.add(md.getSeqStartPosition());
                        rowData.add(md.getRefAllele());
                        rowData.add(md.getAltAllele());
                        rowData.add(md.getUcscGeneId());
                        rowData.add(md.getNcbiGeneId());
                        rowData.add(md.getNcbiTranscriptId());
                        rowData.add(md.getEnsemblGeneId());
                        rowData.add(md.getEnsemblTranscriptId());
                        rowData.add(md.getExistingVariations());
                        rowData.add(md.getGenomeAssembly());
                        rowData.add(mc.getPlatform().getName());
                        break;
                    case "copy number alteration":
                        rowData.add(md.getChromosome());
                        rowData.add(md.getSeqStartPosition());
                        rowData.add(md.getSeqEndPosition());
                        rowData.add(md.getMarker());
                        rowData.add(md.getUcscGeneId());
                        rowData.add(md.getNcbiGeneId());
                        rowData.add(md.getEnsemblGeneId());
                        rowData.add(md.getCnaLog10RCNA());
                        rowData.add(md.getCnaLog2RCNA());
                        rowData.add(md.getFold_change());
                        rowData.add(md.getCnaCopyNumberStatus());
                        rowData.add(md.getCnaGisticValue());
                        rowData.add(md.getCnaPicnicValue());
                        rowData.add(md.getGenomeAssembly());
                        rowData.add(mc.getPlatform().getName());
                        break;
                    case "cytogenetics":
                        rowData.add("");
                        rowData.add(md.getMarker());
                        rowData.add(md.getCytogeneticsResult());
                        rowData.add(md.getMarkerStatusComment());
                        rowData.add(mc.getPlatform().getName());
                        rowData.add("");
                        rowData.add("");
                        break;
                    case "expression":
                        rowData.add(md.getChromosome());
                        rowData.add("");
                        rowData.add(md.getSeqStartPosition());
                        rowData.add(md.getSeqEndPosition());
                        rowData.add(md.getMarker());
                        rowData.add("");
                        rowData.add("");
                        rowData.add("");
                        rowData.add(md.getRnaSeqCoverage());
                        rowData.add(md.getRnaSeqFPKM());
                        rowData.add(md.getRnaSeqTPM());
                        rowData.add(md.getRnaSeqCount());
                        rowData.add(md.getAffyHGEAProbeId());
                        rowData.add(md.getAffyHGEAExpressionValue());
                        rowData.add(md.getIlluminaHGEAProbeId());
                        rowData.add(md.getIlluminaHGEAExpressionValue());
                        rowData.add(md.getZscore());
                        rowData.add(md.getGenomeAssembly());
                        rowData.add(mc.getPlatform().getName());
                        break;
                    default:
                        throw new IllegalArgumentException("Inappropriate Molecular data type passed");
                }
                sheetData.add(rowData);
            }
        }
    }

    public void updateSheetWithData(Sheet sheet, List<List<String>> data, int startRow, int startColumn) {

        //Workbook rows and cells start at index 0
        if (data != null) {

            for (int i = 0; i < data.size(); i++) {

                int rowIndex = startRow + i - 1;
                sheet.createRow(rowIndex);

                for (int j = 0; j < data.get(i).size(); j++) {


                    int columnIndex = startColumn + j - 1;
                    sheet.getRow(rowIndex).createCell(columnIndex);


                    Cell cell;
                    try {
                        cell = sheet.getRow(rowIndex).getCell(columnIndex);
                        cell.setCellValue(data.get(i).get(j));
                    } catch (Exception e) {

                        log.error("Exception in {}  {}:{}", sheet.getSheetName(), rowIndex, columnIndex);
                    }
                }
            }
        }
    }

    public void readSheetAndWriteOmicTsvFile(Sheet xlsxTemplate, List<List<String>> exportSheet, String omicTsvDir) {

        try(FileWriter fileWriter = new FileWriter(omicTsvDir)) {
            if (exportSheet != null) {
                copyHeadersFromSheetToTsv(xlsxTemplate,fileWriter);
                writeDataToTsv(xlsxTemplate, exportSheet, fileWriter);
            }
        } catch(Exception e) {
            log.error(String.format("IO Error from reading omic TSV %s",e.toString()));
        }
    }


    private void copyHeadersFromSheetToTsv(Sheet xlsxTemplate, FileWriter fileWriter) throws IOException {

        for (int j = 0; j < xlsxTemplate.getRow(0).getLastCellNum(); j++) {
            Cell cell;
            try {
                cell = xlsxTemplate.getRow(0).getCell(j);
                fileWriter.append(cell.toString());
                fileWriter.append("\t");
            } catch (Exception e) {
                log.error("Exception in loading export headers");
            }
        }
        fileWriter.append("\n");
    }

    private void writeDataToTsv(Sheet sheet, List<List<String>> data, FileWriter fileWriter) throws IOException {
        for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < data.get(rowIndex).size(); columnIndex++) {
                try {
                    fileWriter.append(data.get(rowIndex).get(columnIndex));
                    fileWriter.append("\t");
                } catch (IOException e) {
                    log.error("Exception in {}  {}:{}", sheet.getSheetName(), rowIndex, columnIndex);
                }
            }
            fileWriter.append("\n");
        }
    }


    private String getPubmedIDs(ModelCreation model){

        StringBuilder pubmedIDs = new StringBuilder();

        if(model.getGroups() != null){

            for(Group g : model.getGroups()){

                if(g.getType().equals("Publication")){

                    if(pubmedIDs.length() != 0){
                        pubmedIDs.append(",");
                    }

                    pubmedIDs.append(g.getPubMedId());
                }
            }
        }

        return pubmedIDs.toString();
    }

    private void insertModelSheetDataFromSpecimenMap(Map<String, ModelDetails> specimenMap, ModelCreation model){

        String modelId = model.getSourcePdxId();

        String pubmedIDs = getPubmedIDs(model);

        for(Map.Entry<String, ModelDetails> entry : specimenMap.entrySet()){

            ModelDetails md = entry.getValue();
            List<String> dataRow = new ArrayList<>();

            dataRow.add(modelId);
            dataRow.add(md.getHostStrainName());
            dataRow.add(md.getHostStrainNomenclature());
            dataRow.add(md.getEngraftmentSite());
            dataRow.add(md.getEngraftmentType());
            dataRow.add(md.getEngraftmentMaterial());
            dataRow.add(md.getEngraftmentMaterialStatus());
            dataRow.add(md.getSortedPassages());
            dataRow.add(pubmedIDs);

            pdxModelSheetDataExport.add(dataRow);
        }
    }

    private void insertSharingAndContactDataForModel(LinkedHashMap<String, String> sharingAndContactRow){

        if(sharingAndContactRow != null) {

            List<String> dataRow = new ArrayList<>();

            dataRow.add(sharingAndContactRow.get("modelId"));
            dataRow.add(sharingAndContactRow.get("providerType"));
            dataRow.add(sharingAndContactRow.get("modelAccessibility"));
            dataRow.add(sharingAndContactRow.get("accessModalities"));
            dataRow.add(sharingAndContactRow.get("contactEmail"));
            dataRow.add(sharingAndContactRow.get("contactName"));
            dataRow.add(sharingAndContactRow.get("contactLink"));
            dataRow.add(sharingAndContactRow.get("modelLink"));
            dataRow.add(sharingAndContactRow.get("providerName"));
            dataRow.add(sharingAndContactRow.get("providerAbbrev"));
            dataRow.add(sharingAndContactRow.get("projectName"));

            sharingAndContactSheetDataExport.add(dataRow);
        }
    }

    private Group getGroupByType(ModelCreation model, String type){

        if(model.getGroups() != null){

            for(Group g : model.getGroups()){

                if(g.getType().equals(type)){
                    return g;
                }
            }
        }

        return null;
    }

    private void getGroupData(LinkedHashMap<String, String> map, Group providerGroup, Group accessGroup, Group projectGroup){

        if(providerGroup != null) {
            map.put("providerType", providerGroup.getProviderType());
            map.put("providerName", providerGroup.getName());
            map.put("providerAbbrev", providerGroup.getAbbreviation());
        }
        else{
            map.put("providerType", "");
            map.put("providerName", "");
            map.put("providerAbbrev", "");
        }
        if(accessGroup != null){
            map.put("modelAccessibility", accessGroup.getAccessibility());
            map.put("accessModalities", accessGroup.getAccessModalities());
        }
        else{
            map.put("modelAccessibility", "");
            map.put("accessModalities", "");
        }

        if(projectGroup != null){
            map.put("projectName", projectGroup.getName());
        }
        else {
            map.put("projectName", "");
        }
    }

    private void getExternalUrlData(LinkedHashMap<String, String> map, Collection<ExternalUrl> urls){

        map.put("contactEmail",  "");
        map.put("contactName",  "");
        map.put("contactLink",  "");
        map.put("modelLink",  "");

        if(urls != null){

            for(ExternalUrl ex: urls) {

                if (ex.getType().equals("contact")) {

                    if (ex.getUrl() != null && ex.getUrl().contains("@")) {
                        map.put("contactEmail", ex.getUrl());
                    } else {
                        map.put("contactLink", ex.getUrl());
                    }
                } else if (ex.getType().equals("source") && ex.getUrl() != null) {
                    map.put("modelLink", ex.getUrl());
                }
            }
        }
    }

    private void addEntryToSpecimenMap(
        Map<String, ModelDetails> specimenMap,
        HostStrain hostStrain,
        String engraftmentSite,
        String engraftmentType,
        String engraftmentMaterial,
        String engraftmentMaterialStatus,
        String passage){

        String specimenMapKey = String.join(
            hostStrain.getName(),
            hostStrain.getSymbol(),
            engraftmentSite,
            engraftmentType,
            engraftmentMaterial,
            engraftmentMaterialStatus);

        if(specimenMap.containsKey(specimenMapKey)){
            specimenMap.get(specimenMapKey).getPassages().add(passage);
        }
        else{

            ModelDetails md = new ModelDetails(
                hostStrain.getName(),
                hostStrain.getSymbol(),
                engraftmentSite,
                engraftmentType,
                engraftmentMaterial,
                engraftmentMaterialStatus,
                passage);
            specimenMap.put(specimenMapKey, md);
        }
    }

    private void addPatientMolcharDataToSamplePlatform(ModelCreation model){

        //get the patient sample related molchars first
        if(model.getSample() != null && model.getSample().getMolecularCharacterizations() != null){

            for(MolecularCharacterization mc : model.getSample().getMolecularCharacterizations()){

                List<String> dataRow = new ArrayList<>();

                dataRow.add("");
                dataRow.add(model.getSample().getSourceSampleId());
                dataRow.add(patientOrigin);
                dataRow.add("NA");
                dataRow.add("");
                dataRow.add(model.getSourcePdxId());
                dataRow.add("");
                dataRow.add("");

                dataRow.add(mc.getType());
                dataRow.add(mc.getPlatform().getName());
                dataRow.add(mc.getTechnology());
                dataRow.add("");
                dataRow.add("");
                dataRow.add("");
                dataRow.add("");
                dataRow.add("");
                dataRow.add(mc.getPlatform().getUrl());

                samplePlatformDescriptionSheetDataExport.add(dataRow);
            }
        }
    }

    private void addXenoMolcharDataToSamplePlatform(ModelCreation model){

        if(model.getSpecimens() != null){

            for(Specimen sp : model.getSpecimens()){

                String passage = sp.getPassage();

                String hostStrainName = "";
                String hostStrainNomenclature = "";

                if(sp.getHostStrain() != null){
                    hostStrainName = getHostStrainName(sp);
                    hostStrainNomenclature = getHostStrainNomenclature(sp);
                }


                Sample sample = sp.getSample();

                if(sample != null && sample.getMolecularCharacterizations() != null && !sample.getMolecularCharacterizations().isEmpty()){

                    for(MolecularCharacterization mc : sample.getMolecularCharacterizations()) {

                        List<String> dataRow = new ArrayList<>();

                        dataRow.add("");
                        dataRow.add(sample.getSourceSampleId());
                        dataRow.add("xenograft");
                        dataRow.add(passage);
                        dataRow.add("");
                        dataRow.add(model.getSourcePdxId());
                        dataRow.add(hostStrainName);
                        dataRow.add(hostStrainNomenclature);
                        dataRow.add(mc.getType());
                        dataRow.add(mc.getPlatform().getName());
                        dataRow.add(mc.getTechnology());
                        dataRow.add("");
                        dataRow.add("");
                        dataRow.add("");
                        dataRow.add("");
                        dataRow.add("");
                        dataRow.add(mc.getPlatform().getUrl());

                        samplePlatformDescriptionSheetDataExport.add(dataRow);
                    }
                }
            }
        }

    }

    private void initPatientGenomicData(ModelCreation model, List<List<String>> sheetData){

        if(model.getSample() != null && model.getSample().getMolecularCharacterizations() != null){

            String sampleId = model.getSample().getSourceSampleId();

            for(MolecularCharacterization mc : model.getSample().getMolecularCharacterizations()){

                insertOmicDataToSheet(model, sampleId, patientOrigin, mc.getType(), null, mc, sheetData);
            }
        } else {
            log.error("No molecular data on patient sample found for model {} \n", model.getSourcePdxId());
        }
    }

    private void initXenoGenomicData(ModelCreation model, List<List<String>> sheetData){

        //then look for xenograft molchars
        if(model.getSpecimens()!= null){

            for(Specimen sp : model.getSpecimens()){

                if(sp.getSample() != null && sp.getSample().getMolecularCharacterizations() != null){

                    String sampleId = sp.getSample().getSourceSampleId();

                    for(MolecularCharacterization mc: sp.getSample().getMolecularCharacterizations()){

                        insertOmicDataToSheet(model, sampleId,"xenograft", mc.getType(), sp, mc, sheetData);
                    }
                }
            }
        } else log.error("No specimens found for model {}", model.getId());
    }


    private XSSFWorkbook getWorkbook(String templatePath) {

        File file = new File(templatePath);
        log.debug("Loading template {}", templatePath);
        if (!file.exists()) {
            log.error("Template not found {}", templatePath);
            return null;
        }


        try (FileInputStream fileInputStream = new FileInputStream(file)) {

            return new XSSFWorkbook(fileInputStream);

        } catch (IOException e) {
            log.error("There was a problem accessing the file: {}", file, e);
        }
        return null;
    }

    private String getHostStrainName(Specimen sp){

        return sp.getHostStrain().getName() == null?"":sp.getHostStrain().getName();
    }

    private String getHostStrainNomenclature(Specimen sp){
        return sp.getHostStrain().getSymbol() == null?"":sp.getHostStrain().getSymbol();
    }

    public List<List<String>> getPatientSheetDataExport() {
        return patientSheetDataExport;
    }

    public List<List<String>> getPatientTumorSheetDataExport() {
        return patientTumorSheetDataExport;
    }

    public List<List<String>> getPatientTreatmentSheetDataExport() {
        return patientTreatmentSheetDataExport;
    }

    public List<List<String>> getPdxModelSheetDataExport() {
        return pdxModelSheetDataExport;
    }

    public List<List<String>> getPdxModelValidationSheetDataExport() {
        return pdxModelValidationSheetDataExport;
    }

    public List<List<String>> getSamplePlatformDescriptionSheetDataExport() {
        return samplePlatformDescriptionSheetDataExport;
    }

    public List<List<String>> getSharingAndContactSheetDataExport() {
        return sharingAndContactSheetDataExport;
    }

    public List<List<String>> getCytogeneticsSheetDataExport() {
        return cytogeneticsSheetDataExport;
    }

    public List<List<String>> getLoaderRelatedDataSheetDataExport() {
        return loaderRelatedDataSheetDataExport;
    }

    public List<List<String>> getDrugDosingSheetDataExport() {
        return drugDosingSheetDataExport;
    }

    public void setMutationSheetDataExport(List<List<String>> mutationSheetDataExport) {
        this.mutationSheetDataExport = mutationSheetDataExport;
    }

    public List<List<String>> getMutationSheetDataExport() {
        return mutationSheetDataExport;
    }

    public List<List<String>> getCnaSheetDataExport() {
        return cnaSheetDataExport;
    }

    public void setCnaSheetDataExport(List<List<String>> cnaSheetDataExport) {
        this.cnaSheetDataExport = cnaSheetDataExport;
    }

    public void setPatientSheetDataExport(List<List<String>> patientSheetDataExport) {
        this.patientSheetDataExport = patientSheetDataExport;
    }

    public void setPatientTumorSheetDataExport(List<List<String>> patientTumorSheetDataExport) {
        this.patientTumorSheetDataExport = patientTumorSheetDataExport;
    }

    public void setPatientTreatmentSheetDataExport(List<List<String>> patientTreatmentSheetDataExport) {
        this.patientTreatmentSheetDataExport = patientTreatmentSheetDataExport;
    }

    public void setPdxModelSheetDataExport(List<List<String>> pdxModelSheetDataExport) {
        this.pdxModelSheetDataExport = pdxModelSheetDataExport;
    }

    public void setPdxModelValidationSheetDataExport(List<List<String>> pdxModelValidationSheetDataExport) {
        this.pdxModelValidationSheetDataExport = pdxModelValidationSheetDataExport;
    }

    public void setSamplePlatformDescriptionSheetDataExport(List<List<String>> samplePlatformDescriptionSheetDataExport) {
        this.samplePlatformDescriptionSheetDataExport = samplePlatformDescriptionSheetDataExport;
    }

    public void setSharingAndContactSheetDataExport(List<List<String>> sharingAndContactSheetDataExport) {
        this.sharingAndContactSheetDataExport = sharingAndContactSheetDataExport;
    }

    public void setCytogeneticsSheetDataExport(List<List<String>> cytogeneticsSheetDataExport) {
        this.cytogeneticsSheetDataExport = cytogeneticsSheetDataExport;
    }

    public void setLoaderRelatedDataSheetDataExport(List<List<String>> loaderRelatedDataSheetDataExport) {
        this.loaderRelatedDataSheetDataExport = loaderRelatedDataSheetDataExport;
    }

    public void setDrugDosingSheetDataExport(List<List<String>> drugDosingSheetDataExport) {
        this.drugDosingSheetDataExport = drugDosingSheetDataExport;
    }


}
