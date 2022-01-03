package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Person;
import mk.ukim.finki.masterapplicationsystem.domain.StepValidation;
import mk.ukim.finki.masterapplicationsystem.domain.Validation;
import mk.ukim.finki.masterapplicationsystem.domain.ValidationStatus;
import mk.ukim.finki.masterapplicationsystem.repository.StepValidationRepository;
import mk.ukim.finki.masterapplicationsystem.service.StepService;
import mk.ukim.finki.masterapplicationsystem.service.StepValidationService;

import java.util.List;

public class StepValidationServiceImpl implements StepValidationService {
    private StepValidationRepository stepValidationRepository;
    private StepService stepService;

    public StepValidationServiceImpl(StepValidationRepository stepValidationRepository, StepService stepService) {
        this.stepValidationRepository = stepValidationRepository;
        this.stepService = stepService;
    }

    @Override
    public StepValidation findById(String id) {
        return stepValidationRepository.findById(id).orElseThrow(() -> new RuntimeException("Step validation with id " + id + " was not found"));
    }

    @Override
    public List<StepValidation> findByValidationId(String validationId) {
        return stepValidationRepository.findByValidationId(validationId);
    }

    @Override
    public StepValidation save(String validationId, Person person, ValidationStatus validationStatus) {
        // TODO: Check if this action can be done
        Validation validation = (Validation) stepService.findStepById(validationId);
        StepValidation stepValidation = new StepValidation(person, validation, validationStatus);
        return stepValidationRepository.save(stepValidation);
    }

    @Override
    public List<StepValidation> findAllByStatus(ValidationStatus validationStatus) {
        return stepValidationRepository.findAllByValidationStatus(validationStatus);
    }

    @Override
    public List<StepValidation> findAllByValidationIdAndStatus(String validationId, ValidationStatus validationStatus) {
        return stepValidationRepository.findAllByValidationIdAndValidationStatus(validationId, validationStatus);
    }

    @Override
    public List<StepValidation> findAllByPersonId(String personId) {
        return stepValidationRepository.findAllByPersonId(personId);
    }

}
