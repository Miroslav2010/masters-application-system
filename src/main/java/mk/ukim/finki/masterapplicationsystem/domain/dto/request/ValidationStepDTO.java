package mk.ukim.finki.masterapplicationsystem.domain.dto.request;

import lombok.Getter;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ValidationStatus;

@Getter
public class ValidationStepDTO {

    private String processId;
    private ValidationStatus validationStatus;

}
