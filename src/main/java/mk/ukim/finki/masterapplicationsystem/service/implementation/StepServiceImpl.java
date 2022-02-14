package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Process;
import mk.ukim.finki.masterapplicationsystem.domain.*;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState;
import mk.ukim.finki.masterapplicationsystem.repository.StepRepository;
import mk.ukim.finki.masterapplicationsystem.service.DocumentService;
import mk.ukim.finki.masterapplicationsystem.service.PersonService;
import mk.ukim.finki.masterapplicationsystem.service.StepService;
import mk.ukim.finki.masterapplicationsystem.service.StepValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState.*;

@Service
public class StepServiceImpl implements StepService {
    private final StepRepository stepRepository;
    private final DocumentService documentService;
    private final ProcessServiceImpl processService;
    private final ProcessStateHelperService processStateHelperService;
    private final StepValidationService stepValidationService;
    private final PersonService personService;
//    private final RemarkService remarkService;

    public StepServiceImpl(StepRepository stepRepository, DocumentService documentService, ProcessServiceImpl processService, ProcessStateHelperService processStateHelperService, StepValidationService stepValidationService, PersonService personService) {
        this.stepRepository = stepRepository;
        this.documentService = documentService;
        this.processService = processService;
        this.processStateHelperService = processStateHelperService;
        this.stepValidationService = stepValidationService;
//        this.remarkService = remarkService;
        this.personService = personService;
    }

    private final Logger logger = LoggerFactory.getLogger(StepServiceImpl.class);

    @Override
    public List<Step> findAllSteps(String processId) {
        return stepRepository.findAllByProcessId(processId);
    }

    @Override
    public List<Step> findAllLastInstanceSteps(String processId) {
        Step step = getActiveStep(processId);
        List<Step> steps = new ArrayList<>();
        Arrays.stream(values()).forEach(s -> steps.addAll(stepRepository.findAllByProcessIdAndName(processId, s.toString())));
        return steps.stream().filter(s-> !s.getId().equals(step.getId()))
                .sorted(Comparator.comparing(Step::getOrderNumber)).collect(Collectors.toList());
    }

    @Override
    public Step findHistoryStepById(String stepId) {
        Step step = findStepById(stepId);
        step.setProcess(null);
        return step;
    }

    @Override
    public Step findStepById(String id) {
        return stepRepository.findById(id).orElseThrow(() -> new RuntimeException("Step with id " + id + " was not found"));
    }

    @Override
    public Step getActiveStep(String processId) {
        return findAllSteps(processId).stream().max(Comparator.comparing(Step::getOrderNumber))
                .orElseThrow(() -> new RuntimeException(String.format("Step of the processId %s was not found", processId)));
    }

    @Override
    public boolean doesStepExist(String processId, String name) {
        return stepRepository.findFirstByProcessIdAndNameOrderByOrderNumberDesc(processId, name).isPresent();
    }

    @Override
    public Step getStepFromProcess(String processId, String name) {
        return stepRepository.findFirstByProcessIdAndNameOrderByOrderNumberDesc(processId, name)
                .orElseThrow(() -> new RuntimeException(String.format("Step with name %s for the process with id %s was not found", name, processId)));
    }

    private Step createNewStep(String processId) {
        int stepOrderNumber = 1;
        if (findAllSteps(processId).size() != 0)
            stepOrderNumber = getActiveStep(processId).getOrderNumber() + 1;
        Process process = processService.findProcessById(processId);
        Step newStep = new Step(stepOrderNumber, process.getProcessState().toString(), process);
        return newStep;
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
        createStepValidation(processId, validation);
        return validation;
    }

    private void createStepValidation(String processId, Validation validation) {
        processStateHelperService.getResponsiblePersonsForStep(processId)
                .forEach(person -> stepValidationService.createStepValidation(validation, person));
    }

    @Override
    public MasterTopic getMasterTopicFromProcess(String processId) {
        return (MasterTopic) stepRepository.findAllByProcessIdAndName(processId, DOCUMENT_APPLICATION.toString())
                .stream().max(Comparator.comparing(Step::getOrderNumber))
                .orElseThrow(() -> new RuntimeException("There is not a master topic step with name " + DOCUMENT_APPLICATION + " for process with id " + processId));
    }

    @Override
    public MasterTopic saveMasterTopic(String processId, String userId, String topic, String description,
                                       Document biography, Document mentorApproval, Document application,
                                       Document supplement) {
        // we create master topic for every DOCUMENT_APPLICATION step
        MasterTopic masterTopic = new MasterTopic(createNewStep(processId), topic, description, application,
                mentorApproval, biography, supplement);
        logger.info("Saved topic for master with process id: {}", processId);
        return stepRepository.save(masterTopic);
    }

    @Override
    public MasterTopic editMasterTopic(String processId, String userId, String topic, String description, MultipartFile application, MultipartFile mentorApproval, MultipartFile biography, MultipartFile supplement) throws IOException {
        Document biographyDocument = documentService.saveApplicationDocument(userId, biography);
        Document mentorApprovalDocument = documentService.saveApplicationDocument(userId, mentorApproval);
        Document applicationDocument = documentService.saveApplicationDocument(userId, application);
        Document supplementDocument = documentService.saveApplicationDocument(userId, supplement);
        MasterTopic lastMasterTopic = getMasterTopicFromProcess(processId);
        lastMasterTopic.setTopic(topic);
        lastMasterTopic.setDescription(description);
        lastMasterTopic.setBiography(biographyDocument);
        lastMasterTopic.setMentorApproval(mentorApprovalDocument);
        lastMasterTopic.setApplication(applicationDocument);
        lastMasterTopic.setSupplement(supplementDocument);
        return stepRepository.save(lastMasterTopic);
    }

    @Override
    public MasterTopic editMasterTopicBiography(String processId, MultipartFile file) throws IOException {
        MasterTopic masterTopic = getMasterTopicFromProcess(processId);
        Document biography = documentService.saveApplicationDocument(new Student().getId(), file);
        masterTopic.setBiography(biography);
        masterTopic = stepRepository.save(masterTopic);
        logger.info("Edited biography for master with process id: %s", processId);
        return masterTopic;
    }

    @Override
    public MasterTopic editMasterTopicMentorApproval(String processId, MultipartFile file) throws IOException {
        MasterTopic masterTopic = getMasterTopicFromProcess(processId);
        Document mentorApproval = documentService.saveApplicationDocument(new Student().getId(), file);
        masterTopic.setMentorApproval(mentorApproval);
        return stepRepository.save(masterTopic);
    }

    @Override
    public MasterTopic editMasterTopicApplication(String processId, MultipartFile file) throws IOException {
        MasterTopic masterTopic = getMasterTopicFromProcess(processId);
        Document application = documentService.saveApplicationDocument(new Student().getId(), file);
        masterTopic.setApplication(application);
        return stepRepository.save(masterTopic);
    }

    @Override
    public MasterTopic editMasterTopicSupplement(String processId, MultipartFile file) throws IOException {
        MasterTopic masterTopic = getMasterTopicFromProcess(processId);
        Document supplement = documentService.saveApplicationDocument(new Student().getId(), file);
        masterTopic.setSupplement(supplement);
        return stepRepository.save(masterTopic);
    }

    @Override
    public Attachment getAttachmentFromProcess(String processId, String name) {
        return (Attachment) stepRepository.findAllByProcessIdAndName(processId, name).stream().max(Comparator.comparing(Step::getOrderNumber))
                .orElseThrow(() -> new RuntimeException("There is not a attachment step with name " + name + " for process with id " + processId));
    }

    private Document saveDocument(String processId, ProcessState processState, MultipartFile file) {
        try {
            Master master = processService.getProcessMaster(processId);
            if (EnumSet.of(STUDENT_DRAFT, STUDENT_CHANGES_DRAFT).contains(processState)){
                return documentService.saveDraft(master.getStudent().getId(), file);
            }
            return documentService.saveRepost(master.getStudent().getId(), file);
        }
        catch (Exception e) {
            throw new RuntimeException("Save draft failed - log: " + e.getMessage());
        }
    }

    @Override
    public Attachment saveAttachment(String processId, ProcessState processState, String personId, Document draft) {
//        Document draftDocument = saveDocument(personId, processState, draft);
        Attachment attachment = new Attachment(createNewStep(processId), draft);
        attachment = stepRepository.save(attachment);
        logger.info("Saved attachment for process: {}", processId);
        return attachment;
    }

    @Override
    public Attachment initializeAttachment(String processId) {
//        Attachment attachment = new Attachment(createNewStep(processId));
        Master master = processService.getProcessMaster(processId);
        ProcessState processState = processService.getProcessState(processId);
        String processStateName = processState.toString();
        //ako nema ni eden napravi nov incace zemi od prethodniot
        if(!doesStepExist(processId, processStateName)) {
            if(processState != STUDENT_CHANGES_DRAFT) {
                Attachment attachment = new Attachment(createNewStep(processId));
                return stepRepository.save(attachment);
            }
        }
        String personId = "";
        if(EnumSet.of(STUDENT_CHANGES_DRAFT, STUDENT_DRAFT).contains(processState)) {
            processState = doesStepExist(processId, processState.toString()) ? processState : STUDENT_DRAFT;
            personId = master.getStudent().getId();
        }
        else
            personId = master.getMentor().getId();
        // TODO: get files from previous attachment
        Attachment attachment = getAttachmentFromProcess(processId, processState.toString());
        return saveAttachment(processId, processState, personId, attachment.getDocument());
    }

    @Override
    public Attachment editAttachment(String processId, ProcessState processState, String attachmentStepName, MultipartFile file) {
        Attachment attachment = getAttachmentFromProcess(processId, attachmentStepName);
        Document document = saveDocument(processId, processState, file);
        attachment.setDocument(document);
        logger.info("Edited attachment for process: {} with name", processId);
        attachment = stepRepository.save(attachment);
        return attachment;
    }

    @Override
    public Step setClosedDateTime(String stepId, OffsetDateTime closedDateTime) {
        Step step = findStepById(stepId);
        step.setClosed(closedDateTime);
        step = stepRepository.save(step);
        logger.info("Closed step with id: {} at {}", stepId, closedDateTime.toString());
        return step;
    }
}
