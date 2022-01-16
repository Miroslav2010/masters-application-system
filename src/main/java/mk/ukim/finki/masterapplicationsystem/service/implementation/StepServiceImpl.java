package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.*;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState;
import mk.ukim.finki.masterapplicationsystem.repository.StepRepository;
import mk.ukim.finki.masterapplicationsystem.service.DocumentService;
import mk.ukim.finki.masterapplicationsystem.service.StepService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import static mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState.DOCUMENT_APPLICATION;

@Service
public class StepServiceImpl implements StepService {
    private final StepRepository stepRepository;
    private final DocumentService documentService;
    private final ProcessServiceImpl processService;

    public StepServiceImpl(StepRepository stepRepository, DocumentService documentService, ProcessServiceImpl processService) {
        this.stepRepository = stepRepository;
        this.documentService = documentService;
        this.processService = processService;
    }

    private final Logger logger = LoggerFactory.getLogger(StepServiceImpl.class);

    @Override
    public List<Step> findAllSteps(String processId) {
        return stepRepository.findAllByProcessId(processId);
    }

    @Override
    public Step findStepById(String id) {
        return stepRepository.findById(id).orElseThrow(() -> new RuntimeException("Step with id " + id + " was not found"));
    }

    @Override
    public Optional<Step> getActiveStep(String processId) {
        return findAllSteps(processId).stream().max(Comparator.comparing(Step::getOrderNumber));
    }

    @Override
    public Step getStepFromProcess(String processId, String name) {
        return stepRepository.findByProcessIdAndNameOrderByOrderNumberDesc(processId, name)
                .orElseThrow(() -> new RuntimeException(String.format("Step with name %s for the process with id %s was not found", name, processId)));
    }

    private Step createNewStep(String processId) {
        Optional<Step> step = getActiveStep(processId);
        int stepOrderNumber = step.map(value -> value.getOrderNumber() + 1).orElse(1);
        ProcessState processState = processService.getProcessState(processId);
        return new Step(stepOrderNumber, processState.toString());
    }

    @Override
    public Validation getValidationFromProcess(String processId, String name) {
        return (Validation) stepRepository.findAllByProcessIdAndName(processId, name).stream().max(Comparator.comparing(Step::getOrderNumber))
                .orElseThrow(() -> new RuntimeException("There is not a validation step with name " + name + " for process with id " + processId));
    }

    @Override
    public Validation saveValidation(String processId) {
        // TODO: check if this action can be done
        Validation validation = new Validation(createNewStep(processId));
        validation = stepRepository.save(validation);
        logger.info("Validation step saved for process: %s");
        return validation;
    }

    @Override
    public MasterTopic getMasterTopicFromProcess(String processId, String name) {
        return (MasterTopic) stepRepository.findAllByProcessIdAndName(processId, name).stream().max(Comparator.comparing(Step::getOrderNumber))
                .orElseThrow(() -> new RuntimeException("There is not a master topic step with name " + name + " for process with id " + processId));
    }

    @Override
    public MasterTopic saveMasterTopic(String processId, String userId, String topic, String description,
                                       MultipartFile biography, MultipartFile mentorApproval, MultipartFile application,
                                       MultipartFile supplement) throws IOException {
        if (stepRepository.findByProcessIdAndName(processId, DOCUMENT_APPLICATION.toString()).isPresent())
            throw new RuntimeException("There is a master topic with name " + DOCUMENT_APPLICATION + " for process with id " + processId);
        Document biographyDocument = documentService.saveApplicationDocument(userId, biography);
        Document mentorApprovalDocument = documentService.saveApplicationDocument(userId, mentorApproval);
        Document applicationDocument = documentService.saveApplicationDocument(userId, application);
        Document supplementDocument = documentService.saveApplicationDocument(userId, supplement);
        MasterTopic masterTopic = new MasterTopic(createNewStep(processId), topic, description, applicationDocument,
                mentorApprovalDocument, biographyDocument, supplementDocument);
        logger.info("Saved topic for master with process id: %s",processId);
        return stepRepository.save(masterTopic);
    }

    @Override
    public MasterTopic editMasterTopicBiography(String processId, String masterTopicName, MultipartFile file) throws IOException {
        MasterTopic masterTopic = getMasterTopicFromProcess(processId, masterTopicName);
        Document biography = documentService.saveApplicationDocument(new Student().getId(), file);
        masterTopic.setBiography(biography);
        masterTopic = stepRepository.save(masterTopic);
        logger.info("Edited biography for master with process id: %s",processId);
        return masterTopic;
    }

    @Override
    public MasterTopic editMasterTopicMentorApproval(String processId, String masterTopicName, MultipartFile file) throws IOException {
        MasterTopic masterTopic = getMasterTopicFromProcess(processId, masterTopicName);
        Document mentorApproval = documentService.saveApplicationDocument(new Student().getId(), file);
        masterTopic.setMentorApproval(mentorApproval);
        return stepRepository.save(masterTopic);
    }

    @Override
    public MasterTopic editMasterTopicApplication(String processId, String masterTopicName, MultipartFile file) throws IOException {
        MasterTopic masterTopic = getMasterTopicFromProcess(processId, masterTopicName);
        Document application = documentService.saveApplicationDocument(new Student().getId(), file);
        masterTopic.setApplication(application);
        return stepRepository.save(masterTopic);
    }

    @Override
    public MasterTopic editMasterTopicSupplement(String processId, String masterTopicName, MultipartFile file) throws IOException {
        MasterTopic masterTopic = getMasterTopicFromProcess(processId, masterTopicName);
        Document supplement = documentService.saveApplicationDocument(new Student().getId(), file);
        masterTopic.setSupplement(supplement);
        return stepRepository.save(masterTopic);
    }

    @Override
    public Attachment getAttachmentFromProcess(String processId, String name) {
        return (Attachment) stepRepository.findAllByProcessIdAndName(processId, name).stream().max(Comparator.comparing(Step::getOrderNumber))
                .orElseThrow(() -> new RuntimeException("There is not a attachment step with name " + name + " for process with id " + processId));
    }

    @Override
    public Attachment saveAttachment(String processId, String name, MultipartFile draft) throws IOException {
        // TODO: check if this action can be done
        Document draftDocument = documentService.saveApplicationDocument(new Student().getId(), draft);
        Attachment attachment = new Attachment(createNewStep(processId), draftDocument);
        attachment = stepRepository.save(attachment);
        logger.info("Saved attachment for process: %s with name",processId);
        return attachment;
    }

    @Override
    public Attachment initializeAttachment(String processId) {
        Attachment attachment = new Attachment(createNewStep(processId));
        return stepRepository.save(attachment);
    }

    @Override
    public Attachment editAttachment(String processId, String attachmentStepName, MultipartFile file) throws IOException {
        Attachment attachment = getAttachmentFromProcess(processId, attachmentStepName);
        Document document = documentService.saveApplicationDocument(new Student().getId(), file);
        attachment.setDocument(document);
        logger.info("Edited attachment for process: %s with name",processId);
        attachment = stepRepository.save(attachment);
        return attachment;
    }

    @Override
    public Step setClosedDateTime(String stepId, OffsetDateTime closedDateTime) {
        Step step = findStepById(stepId);
        step.setClosed(closedDateTime);
        step  =stepRepository.save(step);
        logger.info("Closed step with id: %s at %s",stepId,closedDateTime.toString());
        return step;

    }
}
