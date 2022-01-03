package mk.ukim.finki.masterapplicationsystem.service;

import mk.ukim.finki.masterapplicationsystem.domain.Person;
import mk.ukim.finki.masterapplicationsystem.domain.StepValidation;
import mk.ukim.finki.masterapplicationsystem.domain.ValidationStatus;

import java.util.List;

public interface StepValidationService {

    StepValidation findById(String id);

    List<StepValidation> findByValidationId(String validationId);

    StepValidation save(String validationId, Person person, ValidationStatus validationStatus);

    List<StepValidation> findAllByStatus(ValidationStatus validationStatus);

    List<StepValidation> findAllByValidationIdAndStatus(String validationId, ValidationStatus validationStatus);

    List<StepValidation> findAllByPersonId(String personId);

}
