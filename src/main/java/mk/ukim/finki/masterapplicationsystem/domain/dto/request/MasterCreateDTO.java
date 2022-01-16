package mk.ukim.finki.masterapplicationsystem.domain.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class MasterCreateDTO {

    private String majorId;
    private String mentorId;
    private String firstCommitteeId;
    private String secondCommitteeId;

}
