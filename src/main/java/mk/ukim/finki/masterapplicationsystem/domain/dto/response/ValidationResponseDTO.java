package mk.ukim.finki.masterapplicationsystem.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ValidationResponseDTO {

    private String stepName;

    private String studentName;

    private List<DocumentDTO> downloadUrl;

}
