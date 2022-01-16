package mk.ukim.finki.masterapplicationsystem.domain.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class DraftDTO {

    private String processId;
    private MultipartFile draft;

}
