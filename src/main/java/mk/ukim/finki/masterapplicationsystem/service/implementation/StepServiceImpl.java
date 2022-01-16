package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.*;
import mk.ukim.finki.masterapplicationsystem.repository.StepRepository;
import mk.ukim.finki.masterapplicationsystem.service.StepService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class StepServiceImpl implements StepService {
    private StepRepository stepRepository;

    public StepServiceImpl(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
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

    private Step createNewStep(String processId, String name) {
        Optional<Step> step = getActiveStep(processId);
        int stepOrderNumber = step.map(value -> value.getOrderNumber() + 1).orElse(1);
        return new Step(stepOrderNumber, name);
    }

    @Override
    public Validation getValidationFromProcess(String processId, String name) {
        return (Validation) stepRepository.findAllByProcessIdAndName(processId, name).stream().max(Comparator.comparing(Step::getOrderNumber))
                .orElseThrow(() -> new RuntimeException("There is not a validation step with name " + name + " for process with id " + processId));
    }

    @Override
    public Validation saveValidation(String processId, String name) {
        // TODO: check if this action can be done
        Validation validation = new Validation(createNewStep(processId, name));
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
    public MasterTopic saveMasterTopic(String processId, String name, String topic, String description,
                                       Document application, Document mentorApproval, Document biography, Document supplement) {
        if (stepRepository.findByProcessIdAndName(processId, name).isPresent())
            throw new RuntimeException("There is a master topic with name " + name + " for process with id " + processId);
        // TODO: Check if there is already a master topic step for process
        MasterTopic masterTopic = new MasterTopic(createNewStep(processId, name), topic, description, application, mentorApproval, biography, supplement);
        // TODO: Viktor integration with document service
        masterTopic = stepRepository.save(masterTopic);
        logger.info("Saved topic for master with process id: %s",processId);
        return masterTopic;
    }

    @Override
    public MasterTopic editMasterTopicBiography(String processId, String masterTopicName, Document biography) {
        MasterTopic masterTopic = getMasterTopicFromProcess(processId, masterTopicName);
        // TODO: Viktor integration with document service
        masterTopic.setBiography(biography);
        masterTopic = stepRepository.save(masterTopic);
        logger.info("Edited biography for master with process id: %s",processId);
        return masterTopic;
    }

    @Override
    public Attachment getAttachmentFromProcess(String processId, String name) {
        return (Attachment) stepRepository.findAllByProcessIdAndName(processId, name).stream().max(Comparator.comparing(Step::getOrderNumber))
                .orElseThrow(() -> new RuntimeException("There is not a attachment step with name " + name + " for process with id " + processId));
    }

    @Override
    public Attachment saveAttachment(String processId, String name, Document document) {
        // TODO: check if this action can be done
        Attachment attachment = new Attachment(createNewStep(processId, name), document);
        // TODO: Viktor integration with document service
        attachment = stepRepository.save(attachment);
        logger.info("Saved attachment for process: %s with name",processId);
        return attachment;
    }

    @Override
    public Attachment editAttachment(String processId, String attachmentStepName, Document document) {
        Attachment attachment = getAttachmentFromProcess(processId, attachmentStepName);
        // TODO: Viktor integration with document service
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
