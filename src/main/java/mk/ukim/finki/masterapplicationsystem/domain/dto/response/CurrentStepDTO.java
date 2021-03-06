package mk.ukim.finki.masterapplicationsystem.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
@AllArgsConstructor
public class CurrentStepDTO {

    private String processState;

    private List<String> assignedPersons;

    private String assignedRole;
}
