package mk.ukim.finki.masterapplicationsystem.service;

import mk.ukim.finki.masterapplicationsystem.domain.Attachment;
import mk.ukim.finki.masterapplicationsystem.domain.Master;
import mk.ukim.finki.masterapplicationsystem.domain.MasterTopic;
import mk.ukim.finki.masterapplicationsystem.domain.Process;
import mk.ukim.finki.masterapplicationsystem.domain.dto.response.ValidationResponseDTO;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ValidationStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MasterManagementService {

    Process createMaster(String mentorId, String firstCommitteeId,
                         String secondCommitteeId, String majorId);

    MasterTopic createMasterTopic(String processId, String topic, String description, MultipartFile biography, MultipartFile mentorApproval, MultipartFile application,
                                  MultipartFile supplement) throws IOException;

    void validateStep(String processId, ValidationStatus validationStatus);

    Master setArchiveNumber(String processId, String archiveNumber);

    Attachment uploadDraft(String processId, MultipartFile draft) throws IOException;

    Process confirmUpload(String processId);

    ValidationResponseDTO getValidationDetails(String processId);


}
