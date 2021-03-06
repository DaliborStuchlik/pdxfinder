package org.pdxfinder.dataloaders.updog.tablevalidation;

import org.apache.commons.collections4.CollectionUtils;
import org.pdxfinder.dataloaders.updog.tablevalidation.error.*;
import org.slf4j.*;
import org.springframework.stereotype.Component;
import tech.tablesaw.api.Table;

import java.util.*;

@Component
public class Validator {

    private static final Logger log = LoggerFactory.getLogger(Validator.class);
    private List<ValidationError> validationErrors;
    private MissingTableErrorCreator missingTableErrorCreator;

    public Validator(
        MissingTableErrorCreator missingTableErrorCreator
    ) {
        this.missingTableErrorCreator = missingTableErrorCreator;
        this.validationErrors = new ArrayList<>();
    }

    public List<ValidationError> validate(
        Map<String, Table> tableSet,
        TableSetSpecification tableSetSpecification
    ) {
        checkRequiredTablesPresent(tableSet, tableSetSpecification);
        if (CollectionUtils.isNotEmpty(validationErrors)) {
            log.error(
                "Not all required tables where present for {}. Aborting further validation",
                tableSetSpecification.getProvider());
            return validationErrors;
        }
        performColumnValidations(tableSet, tableSetSpecification);

        return validationErrors;
    }

    private void performColumnValidations(
        Map<String, Table> tableSet,
        TableSetSpecification tableSetSpecification
    ) {
        checkRequiredColumnsPresent(tableSet, tableSetSpecification);
        checkAllNonEmptyValuesPresent(tableSet, tableSetSpecification);
        checkAllUniqueColumnsForDuplicates(tableSet, tableSetSpecification);
        checkRelationsValid(tableSet, tableSetSpecification);
    }

    private void checkRequiredTablesPresent(
        Map<String, Table> tableSet,
        TableSetSpecification tableSetSpecification
    ) {
        if (tableSetSpecification.hasRequiredColumns())
            validationErrors.addAll(missingTableErrorCreator.generateErrors(tableSet, tableSetSpecification));
    }

    private void checkRequiredColumnsPresent(
        Map<String, Table> tableSet,
        TableSetSpecification tableSetSpecification
    ) {
        if (tableSetSpecification.hasRequiredColumns())
            validationErrors.addAll(new MissingColumnErrorCreator().generateErrors(tableSet, tableSetSpecification));
    }

    private void checkAllNonEmptyValuesPresent(
        Map<String, Table> tableSet,
        TableSetSpecification tableSetSpecification
    ) {
        validationErrors.addAll(new EmptyValueErrorCreator().generateErrors(tableSet, tableSetSpecification));
    }

    private void checkAllUniqueColumnsForDuplicates(
        Map<String, Table> tableSet,
        TableSetSpecification tableSetSpecification
    ) {
        validationErrors.addAll(new DuplicateValueErrorCreator().generateErrors(tableSet, tableSetSpecification));
    }

    private void checkRelationsValid(
        Map<String, Table> tableSet,
        TableSetSpecification tableSetSpecification
    ) {
        validationErrors.addAll(new BrokenRelationErrorCreator().generateErrors(tableSet, tableSetSpecification));
    }

    boolean passesValidation(
        Map<String, Table> tableSet,
        TableSetSpecification tableSetSpecification
    ) {
        return validate(tableSet, tableSetSpecification).isEmpty();
    }

    List<ValidationError> getValidationErrors() {
        return this.validationErrors;
    }

}
