package mk.ukim.finki.masterapplicationsystem.domain.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class MasterTopicStepDTO {
    private String topic;
    private String description;
    private MultipartFile biography;
    private MultipartFile mentorApproval;
    private MultipartFile application;
    private MultipartFile supplement;

}
