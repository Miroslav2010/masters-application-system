package mk.ukim.finki.masterapplicationsystem.service;

import mk.ukim.finki.masterapplicationsystem.domain.Attachment;
import mk.ukim.finki.masterapplicationsystem.domain.MasterTopic;
import mk.ukim.finki.masterapplicationsystem.domain.Step;
import mk.ukim.finki.masterapplicationsystem.domain.Validation;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface StepService {

    List<Step> findAllSteps(String processId);

    Step findStepById(String id);

    Optional<Step> getActiveStep(String processId);

    Step getStepFromProcess(String processId, String name);

    Validation getValidationFromProcess(String processId, String name);

    Validation saveValidation(String processId);

    MasterTopic getMasterTopicFromProcess(String processId, String name);

    MasterTopic saveMasterTopic(String processId, String userId, String topic, String description,
                                MultipartFile application, MultipartFile mentorApproval, MultipartFile biography, MultipartFile supplement) throws IOException;

    MasterTopic editMasterTopicBiography(String processId, String masterTopicName, MultipartFile file) throws IOException;

    MasterTopic editMasterTopicMentorApproval(String processId, String masterTopicName, MultipartFile file) throws IOException;

    MasterTopic editMasterTopicApplication(String processId, String masterTopicName, MultipartFile file) throws IOException;

    MasterTopic editMasterTopicSupplement(String processId, String masterTopicName, MultipartFile file) throws IOException;

    Attachment getAttachmentFromProcess(String processId, String name);

    Attachment saveAttachment(String processId, String name, MultipartFile draft) throws IOException;

    Attachment initializeAttachment(String processId);

    Attachment editAttachment(String processId, String attachmentStepName, MultipartFile file) throws IOException;

    Step setClosedDateTime(String stepId, OffsetDateTime closedDateTime);

}
