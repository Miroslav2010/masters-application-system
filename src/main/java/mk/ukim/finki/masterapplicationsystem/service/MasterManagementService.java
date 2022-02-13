package mk.ukim.finki.masterapplicationsystem.service;

import mk.ukim.finki.masterapplicationsystem.domain.*;
import mk.ukim.finki.masterapplicationsystem.domain.Process;
import mk.ukim.finki.masterapplicationsystem.domain.dto.response.*;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ValidationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MasterManagementService {

    Process createMaster(String studentId, String firstCommitteeId,
                         String secondCommitteeId, String majorId);

    MasterTopic createMasterTopic(String processId, String topic, String description, MultipartFile biography, MultipartFile mentorApproval, MultipartFile application,
                                  MultipartFile supplement) throws IOException;

    void validateStep(String processId, ValidationStatus validationStatus);

    void schedulerValidateStep(String processId, ValidationStatus validationStatus);

    Master setArchiveNumber(String processId, String archiveNumber);

    Attachment uploadDraft(String processId, MultipartFile draft) throws IOException;

    Process confirmUpload(String processId);

    Process cancelRevisionLoop(String processId);

    ValidationResponseDTO getValidationDetails(String processId);

    MasterPreviewListDTO getAllMasters(Pageable pageable, String filter);

    StepPreviewDTO getAllFinishedSteps(String processId);

    StudentMentorDTO getStudentAndMentor(String processId);

    CurrentStepDTO getCurrentStepInfo(String processId);

}
