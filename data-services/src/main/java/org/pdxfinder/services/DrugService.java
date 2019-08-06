package org.pdxfinder.services;

import org.pdxfinder.graph.dao.ModelCreation;
import org.pdxfinder.graph.dao.TreatmentProtocol;
import org.pdxfinder.graph.dao.TreatmentSummary;
import org.pdxfinder.graph.repositories.*;
import org.pdxfinder.services.dto.CountDTO;
import org.springframework.stereotype.Service;

import java.util.*;

/*
 * Created by csaba on 29/03/2018.
 */
@Service
public class DrugService {

    private TreatmentSummaryRepository treatmentSummaryRepository;
    private TreatmentProtocolRepository treatmentProtocolRepository;
    private ResponseRepository responseRepository;
    private ModelCreationRepository modelCreationRepository;

    public DrugService(TreatmentSummaryRepository treatmentSummaryRepository,
                       TreatmentProtocolRepository treatmentProtocolRepository,
                       ResponseRepository responseRepository,
                       ModelCreationRepository modelCreationRepository) {

        this.treatmentSummaryRepository = treatmentSummaryRepository;
        this.treatmentProtocolRepository = treatmentProtocolRepository;
        this.responseRepository = responseRepository;
        this.modelCreationRepository = modelCreationRepository;
    }


    public int getDosingStudiesNumberByDataSource(String dataSource){

        return treatmentSummaryRepository.findDrugDosingStudyNumberByDataSource(dataSource);
    }


    public String getPlatformUrlByDataSource(String dataSource){

        return treatmentSummaryRepository.findPlatformUrlByDataSource(dataSource);
    }

    public List<String> getDrugNames(){

        Set<String> drugNamesSet = new HashSet<>();
        List<String> drugNames = new ArrayList<>();

        List<TreatmentSummary> treatmentSummaries = getModelTreatmentSummariesWithDrugAndResponse();

        for(TreatmentSummary ts : treatmentSummaries){

            for(TreatmentProtocol tp : ts.getTreatmentProtocols()){

                String drugName = tp.getTreatmentString(false);
                drugNamesSet.add(drugName);
            }
        }

        drugNames.addAll(drugNamesSet);
        return drugNames;
    }

    public List<String> getSpecimenDrugResponseOptions(){

        return responseRepository.findAllSpecimenDrugResponses();
    }

    public List<TreatmentSummary> getModelTreatmentSummariesWithDrugAndResponse(){

        return treatmentSummaryRepository.findAllMouseTreatments();
    }

    public int getTotalSummaryNumber(){

        return treatmentSummaryRepository.findTotalSummaryNumber();
    }



    public List<CountDTO> getModelCountByDrugAndComponentType(String type){

        Map<String, Set<String>> results = new HashMap<>();
        Set<ModelCreation> models = modelCreationRepository.getModelsTreatmentsAndDrugs(type);
        List<CountDTO> res = new ArrayList<CountDTO>();

        for(ModelCreation mod : models){
            String modelId = mod.getDataSource() + mod.getSourcePdxId();
            for(TreatmentProtocol tp : mod.getTreatmentSummary().getTreatmentProtocols()){


                String drugName = tp.getTreatmentString(false);

                if(results.containsKey(drugName)){

                    results.get(drugName).add(modelId);
                }
                else{
                    Set s = new HashSet();
                    s.add(modelId);
                    results.put(drugName, s);
                }
            }
        }

        for(Map.Entry<String, Set<String>> entry : results.entrySet()){

            CountDTO c = new CountDTO(entry.getKey(), entry.getValue().size());
            res.add(c);
        }

        Collections.sort(res, (CountDTO c1, CountDTO c2) -> c2.getValue()-c1.getValue());

        return res;

    }

}
