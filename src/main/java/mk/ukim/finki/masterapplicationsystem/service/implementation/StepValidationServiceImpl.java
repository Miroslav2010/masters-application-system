package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Person;
import mk.ukim.finki.masterapplicationsystem.domain.StepValidation;
import mk.ukim.finki.masterapplicationsystem.domain.Validation;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ValidationStatus;
import mk.ukim.finki.masterapplicationsystem.repository.StepValidationRepository;
import mk.ukim.finki.masterapplicationsystem.service.StepService;
import mk.ukim.finki.masterapplicationsystem.service.StepValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StepValidationServiceImpl implements StepValidationService {
    private final StepValidationRepository stepValidationRepository;

    private final Logger logger = LoggerFactory.getLogger(StepValidationServiceImpl.class);

    public StepValidationServiceImpl(StepValidationRepository stepValidationRepository) {
        this.stepValidationRepository = stepValidationRepository;
    }

    @Override
    public StepValidation findById(String id) {
        return stepValidationRepository.findById(id).orElseThrow(() -> new RuntimeException("Step validation with id " + id + " was not found"));
    }

    @Override
    public List<StepValidation> findAllByValidationId(String validationId) {
        return stepValidationRepository.findAllByValidationId(validationId);
    }

    @Override
    public StepValidation createStepValidation(Validation validation, Person person) {
        // TODO: Check if this action can be done
        StepValidation stepValidation = new StepValidation(person, validation, ValidationStatus.WAITING);
        return stepValidationRepository.save(stepValidation);
    }

    @Override
    public StepValidation changeStepValidationStatus(String stepValidationId, ValidationStatus validationStatus) {
        StepValidation stepValidation = findById(stepValidationId);
        stepValidation.setValidationStatus(validationStatus);
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
