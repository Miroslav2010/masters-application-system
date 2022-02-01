package mk.ukim.finki.masterapplicationsystem.repository;

import mk.ukim.finki.masterapplicationsystem.domain.StepValidation;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ValidationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StepValidationRepository extends JpaRepository<StepValidation, String> {

    List<StepValidation> findAllByValidationId(String validationId);

    List<StepValidation> findAllByValidationStatus(ValidationStatus validationStatus);

    List<StepValidation> findAllByValidationIdAndValidationStatus(String validationId, ValidationStatus validationStatus);

    List<StepValidation> findAllByPersonId(String personId);

    Optional<StepValidation> findByValidationIdAndPersonId(String validationId, String personId);
}
