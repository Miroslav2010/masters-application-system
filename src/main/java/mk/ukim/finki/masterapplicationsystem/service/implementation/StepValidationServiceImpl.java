package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Person;
import mk.ukim.finki.masterapplicationsystem.domain.Step;
import mk.ukim.finki.masterapplicationsystem.domain.StepValidation;
import mk.ukim.finki.masterapplicationsystem.domain.Validation;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.Role;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ValidationStatus;
import mk.ukim.finki.masterapplicationsystem.repository.StepValidationRepository;
import mk.ukim.finki.masterapplicationsystem.service.StepValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static mk.ukim.finki.masterapplicationsystem.domain.enumeration.Role.*;

@Service
public class StepValidationServiceImpl implements StepValidationService {
    private final StepValidationRepository stepValidationRepository;

    private final Logger logger = LoggerFactory.getLogger(StepValidationServiceImpl.class);

    public StepValidationServiceImpl(StepValidationRepository stepValidationRepository) {
        this.stepValidationRepository = stepValidationRepository;
    }

    @Override
    public StepValidation findById(String id) {
        return stepValidationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Step validation with id %s was not found", id)));
    }

    @Override
    public StepValidation findByStepIdAndPersonId(String stepId, String personId) {
        return stepValidationRepository.findByValidationIdAndPersonId(stepId, personId)
                .orElseThrow(() -> new RuntimeException(String.format("Step validation with step id %s and person id %s was not found", stepId, personId)));
    }

    @Override
    public ValidationStatus findValidationStatusByStepIdAndPersonId(String stepId, String personId) {
        return findByStepIdAndPersonId(stepId, personId).getValidationStatus();
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
    public StepValidation changeStepValidationStatus(Step validationStep, Person person, ValidationStatus validationStatus) {
        List<Role> roles = person.getRoles();
        StepValidation stepValidation;
        if(roles.contains(STUDENT_SERVICE) || roles.contains(SECRETARY) || roles.contains(NNK) || roles.contains(SYSTEM_USER))
            stepValidation = createStepValidation((Validation) validationStep, person);
        else
            stepValidation = findByStepIdAndPersonId(validationStep.getId(), person.getId());
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
