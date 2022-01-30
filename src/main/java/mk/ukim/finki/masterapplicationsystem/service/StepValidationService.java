package mk.ukim.finki.masterapplicationsystem.service;

import mk.ukim.finki.masterapplicationsystem.domain.Person;
import mk.ukim.finki.masterapplicationsystem.domain.Step;
import mk.ukim.finki.masterapplicationsystem.domain.StepValidation;
import mk.ukim.finki.masterapplicationsystem.domain.Validation;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ValidationStatus;

import java.util.List;

public interface StepValidationService {

    StepValidation findById(String id);

    StepValidation findByStepIdAndPersonId(String stepId, String personId);

    List<StepValidation> findAllByValidationId(String validationId);

    StepValidation createStepValidation(Validation validation, Person person);

    StepValidation changeStepValidationStatus(Step validationStep, Person person, ValidationStatus validationStatus);

    List<StepValidation> findAllByStatus(ValidationStatus validationStatus);

    List<StepValidation> findAllByValidationIdAndStatus(String validationId, ValidationStatus validationStatus);

    List<StepValidation> findAllByPersonId(String personId);

}
