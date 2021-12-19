package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class StepValidation {

    @Id
    private final String id = UUID.randomUUID().toString();

    @ManyToOne
    private Person person;

    @ManyToOne
    private Validation validation;

    @Enumerated(EnumType.STRING)
    private ValidationStatus validationStatus;

    public StepValidation(Person person, Validation validation, ValidationStatus validationStatus) {
        this.person = person;
        this.validation = validation;
        this.validationStatus = validationStatus;
    }
}
