package mk.ukim.finki.masterapplicationsystem.service;

import mk.ukim.finki.masterapplicationsystem.domain.Person;
import mk.ukim.finki.masterapplicationsystem.domain.StepValidation;
import mk.ukim.finki.masterapplicationsystem.domain.Validation;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ValidationStatus;

import java.util.List;

public interface StepValidationService {

    StepValidation findById(String id);

    List<StepValidation> findByValidationId(String validationId);

    StepValidation createStepValidation(Validation validation, Person person);

    StepValidation changeStepValidationStatus(String stepValidationId, ValidationStatus validationStatus);

    List<StepValidation> findAllByStatus(ValidationStatus validationStatus);

    List<StepValidation> findAllByValidationIdAndStatus(String validationId, ValidationStatus validationStatus);

    List<StepValidation> findAllByPersonId(String personId);

}
