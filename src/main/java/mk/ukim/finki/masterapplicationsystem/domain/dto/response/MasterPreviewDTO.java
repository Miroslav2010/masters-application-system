package mk.ukim.finki.masterapplicationsystem.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.OffsetDateTime;

@Data
@Getter
@AllArgsConstructor
public class MasterPreviewDTO {

    private String id;

    private String student;

    private String mentor;

    private String step;

    private String lastModified;

}
