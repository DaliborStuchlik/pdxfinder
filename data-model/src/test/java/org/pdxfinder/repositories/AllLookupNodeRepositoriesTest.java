package org.pdxfinder.repositories;

import org.junit.Before;
import org.junit.Test;
import org.pdxfinder.BaseTest;
import org.pdxfinder.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for the Tumor Type repository
 */
public class AllLookupNodeRepositoriesTest extends BaseTest {

    private final static Logger log = LoggerFactory.getLogger(AllLookupNodeRepositoriesTest.class);

    private static final String TISSUE_NAME = "TEST_TISSUE";
    private static final String BACKGROUND_STRAIN = "TEST_BACKGROUND_STRAIN";
    private static final String IMPLANTATION_TYPE = "TEST_IMPLANTATION_TYPE";
    private static final String IMPLANTATION_SITE = "TEST_IMPLANTATION_SITE";

    @Autowired
    private
    TumorTypeRepository tumorTypeRepository;

    @Autowired
    private TissueRepository tissueRepository;

    @Autowired
    private BackgroundStrainRepository backgroundStrainRepository;

    @Autowired
    private ImplantationSiteRepository implantationSiteRepository;

    @Autowired
    private ImplantationTypeRepository implantationTypeRepository;


    @Before
    public void setupDb() {
    }

    @Rollback(false)
    @BeforeTransaction
    public void cleanDb() {
        tumorTypeRepository.deleteAll();
        tissueRepository.deleteAll();
    }


    @Test
    public void createTumorTypes() throws Exception {

        List<String> types = Arrays.asList("Metastasis", "Metastatic", "Not Specified", "Primary Malignancy", "Recurrent/Relapse");

        for (String type : types) {
            TumorType foundType = tumorTypeRepository.findByName(type);
            if (foundType == null) {
                log.debug("Tumor type {} not found. Creating", type);
                foundType = new TumorType(type);
                tumorTypeRepository.save(foundType);
            }

            foundType = tumorTypeRepository.findByName(type);
            log.info("  Found Tumor type {}", foundType.getName());

            assert (foundType.getName().equals(type));

        }

    }

    @Test
    public void createTissue() {

        Tissue tissue = tissueRepository.findByName(TISSUE_NAME);
        if (tissue == null) {
            log.debug("Tissue {} not found. Creating", TISSUE_NAME);
            tissue = new Tissue(TISSUE_NAME);
            tissueRepository.save(tissue);
        }

        Assert.notNull(tissueRepository.findByName(TISSUE_NAME));
        log.info("  Found Tissue {}", tissueRepository.findByName(TISSUE_NAME).getName());

    }

    @Test
    public void createBackgroundStrain() {

        BackgroundStrain bgStrain = backgroundStrainRepository.findByName(BACKGROUND_STRAIN);
        if (bgStrain == null) {
            log.debug("Background strain {} not found. Creating", BACKGROUND_STRAIN);
            bgStrain = new BackgroundStrain(BACKGROUND_STRAIN);
            backgroundStrainRepository.save(bgStrain);
        }

        Assert.notNull(backgroundStrainRepository.findByName(BACKGROUND_STRAIN));
        log.info("  Found Background Strain {}", backgroundStrainRepository.findByName(BACKGROUND_STRAIN).getName());
    }

    @Test
    public void createImplantationSite() {

        ImplantationSite site = implantationSiteRepository.findByName(IMPLANTATION_SITE);
        if (site == null) {
            log.debug("Implantation site {} not found. Creating", IMPLANTATION_SITE);
            site = new ImplantationSite(IMPLANTATION_SITE);
            implantationSiteRepository.save(site);
        }

        Assert.notNull(implantationSiteRepository.findByName(IMPLANTATION_SITE));
        log.info("  Found Implantation Site {}", implantationSiteRepository.findByName(IMPLANTATION_SITE).getName());
    }

    @Test
    public void createImplantationType() {

        ImplantationType type = implantationTypeRepository.findByName(IMPLANTATION_TYPE);
        if (type == null) {
            log.debug("Implantation type {} not found. Creating", IMPLANTATION_TYPE);
            type = new ImplantationType(IMPLANTATION_TYPE);
            implantationTypeRepository.save(type);
        }

        Assert.notNull(implantationTypeRepository.findByName(IMPLANTATION_TYPE));
        log.info("  Found Implantation Type {}", implantationTypeRepository.findByName(IMPLANTATION_TYPE).getName());

    }

}