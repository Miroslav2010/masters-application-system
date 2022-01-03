package mk.ukim.finki.masterapplicationsystem.service;

import mk.ukim.finki.masterapplicationsystem.domain.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface StepService {

    List<Step> findAllSteps(String processId);

    Step findStepById(String id);

    Optional<Step> getActiveStep(String processId);

    Step getStepFromProcess(String processId, String name);

    Validation getValidationFromProcess(String processId, String name);

    Validation saveValidation(String processId, String name);

    MasterTopic getMasterTopicFromProcess(String processId, String name);

    MasterTopic saveMasterTopic(String processId, String name, String topic, String description,
                                Document application, Document mentorApproval, Document biography, Document supplement);

    MasterTopic editMasterTopicBiography(String processId, String masterTopicName, Document biography);

    Attachment getAttachmentFromProcess(String processId, String name);

    Attachment saveAttachment(String processId, String name, Document document);

    Attachment editAttachment(String processId, String attachmentStepName, Document document);

    Step setClosedDateTime(String stepId, OffsetDateTime closedDateTime);

}
