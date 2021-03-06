package org.pdxfinder.postload;

/*
 * Created by csaba on 15/11/2018.
 */

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.pdxfinder.graph.dao.DataProjection;
import org.pdxfinder.graph.dao.Marker;
import org.pdxfinder.graph.dao.OntologyTerm;
import org.pdxfinder.services.DataImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Order(value = 92)
public class ValidateDB {

    private static Logger log = LoggerFactory.getLogger(ValidateDB.class);

    private DataImportService dataImportService;


    public ValidateDB(DataImportService dataImportService) {
        this.dataImportService = dataImportService;
    }

    public void run() {

        log.info("******************************************************");
        log.info("* Starting validation checks                         *");
        log.info("******************************************************");

        runValidationChecks();

    }


    private void runValidationChecks(){


        /*
            Scenarios when DB is invalid:
            - Missing DataProjection nodes
            - Patients with multiple treatmentsummaries

            Scenarios when DB is valid with warnings:
            - Found nodes without relationships and they are not DataProjections
            - Platforms without url attribute value
         */




        boolean isDBValid = true;
        boolean noWarnings = true;

        //check if there is any unlinked nodes that are not dataprojections


        log.info("Looking for sad nodes (nodes without a relationship)");
        Set<String> unlinkedNodeTypeSet = new HashSet<>();
        /*
        //this  is disabled now, needs to be batched

        Set<Object> unlinkedNodes = dataImportService.findUnlinkedNodes();

        if(unlinkedNodes.size() == 0){
            log.error("DataProjection nodes not found - invalid database!");
            isDBValid = false;
        }
        else{

            for(Object node : unlinkedNodes){

                if(!(node instanceof DataProjection || node instanceof Marker || node instanceof OntologyTerm) ){
                    //Uh-oh! We found a lonely node that is not DataProjection or Marker!!!
                    unlinkedNodeTypeSet.add(node.getClass().getSimpleName());
                }
            }
        }

        */


        //check if there is any patient with multiple treatment summaries
        log.info("Looking for patients with multiple treatment summaries");
        Set<Object> patientsWithMultipleSummaries = dataImportService.findPatientsWithMultipleSummaries();

        if(patientsWithMultipleSummaries.size() > 0){
            isDBValid = false;

            log.error("Found patients with multiple treatment summaries!");
        }

        //check if there is any platform without url
        log.info("Validating platforms");
        Set<Object> platformsWithoutUrl = dataImportService.findPlatformsWithoutUrl();

        if(platformsWithoutUrl.size() > 0){

            noWarnings = false;
            log.warn("Found Platforms without url: "+ platformsWithoutUrl.toString());
        }



        //TODO: Check if DataProjection nodes are there
        //TODO: Define additional db checks!



        //list errors
        if(unlinkedNodeTypeSet.size() > 0){
            noWarnings = false;
            log.warn("Node types found without a relationship: "+unlinkedNodeTypeSet.toString());
            log.warn("Get those nodes a relationship!");
        }


        //inform user
        if(noWarnings && isDBValid){

            log.info("*******************************************************");
            log.info("* Finished DB validation: VALID DATABASE, NO WARNINGS *");
            log.info("*******************************************************");
        }
        else if(!noWarnings){
            log.warn("********************************************************");
            log.warn("* Finished DB validation: VALID DATABASE WITH WARNINGS *");
            log.warn("********************************************************");
        }
        else if(!isDBValid){

            log.error("*******************************************************");
            log.error("* Finished DB validation: INVALID DATABASE            *");
            log.error("*******************************************************");
        }



    }

}
