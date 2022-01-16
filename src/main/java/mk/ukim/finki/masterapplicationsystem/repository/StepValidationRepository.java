package mk.ukim.finki.masterapplicationsystem.repository;

import mk.ukim.finki.masterapplicationsystem.domain.StepValidation;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ValidationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StepValidationRepository extends JpaRepository<StepValidation, String> {

    List<StepValidation> findByValidationId(String validationId);

    List<StepValidation> findAllByValidationStatus(ValidationStatus validationStatus);

    List<StepValidation> findAllByValidationIdAndValidationStatus(String validationId, ValidationStatus validationStatus);

    List<StepValidation> findAllByPersonId(String personId);
}
