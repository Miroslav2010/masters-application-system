package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Process;
import mk.ukim.finki.masterapplicationsystem.domain.*;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ValidationStatus;
import mk.ukim.finki.masterapplicationsystem.service.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.EnumSet;

import static mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState.*;

@Service
public class MasterManagementServiceImpl implements MasterManagementService {
    private final MasterService masterService;
    private final ProcessService processService;
    private final StepService stepService;
    private final PersonService personService;
    private final MajorService majorService;
    private final StepValidationService stepValidationService;
    private final PermissionService permissionService;

    public MasterManagementServiceImpl(MasterService masterService, ProcessService processService, StepService stepService,
                                       PersonService personService, MajorService majorService, StepValidationService stepValidationService, ProcessStateHelperService processStateHelperService, PermissionService permissionService) {
        this.masterService = masterService;
        this.processService = processService;
        this.stepService = stepService;
        this.personService = personService;
        this.majorService = majorService;
        this.stepValidationService = stepValidationService;
        this.permissionService = permissionService;
    }

    private Professor findProfessorById(String id) {
        return (Professor) personService.getPerson(id);
    }

    private void setUpNewValidation(String processId) {
        // for student service, secretary and nnk we do not assign people
        stepService.saveValidation(processId);
    }

    private void setUpDraftStep(String processId) {
        stepService.initializeAttachment(processId);
    }

    private void setUpNewStep(String processId) {
        ProcessState processState = processService.getProcessState(processId);
        if (EnumSet.of(STUDENT_DRAFT, STUDENT_CHANGES_DRAFT, APPLICATION_FINISHED, FINISHED).contains(processState))
            setUpDraftStep(processId);
        else
            setUpNewValidation(processId);
    }

    private Process processNextState(String processId) {
        return processService.nextState(processId);
    }

    @Override
    public Process createMaster(String mentorId, String firstCommitteeId, String secondCommitteeId, String majorId) {
        Professor mentor = findProfessorById(mentorId);
        Professor firstCommittee = findProfessorById(firstCommitteeId);
        Professor secondCommittee = findProfessorById(secondCommitteeId);
        Major major = majorService.findMajorById(majorId);
        // TODO: here we should get the active user
        Master master = masterService.saveMasterWithAllData((Student) personService.getAll().stream().sorted(Comparator.comparing(Person::getFullName)).findFirst().get(),
                mentor, firstCommittee, secondCommittee, major);
        Process masterProcess = processService.save(master);
        return processNextState(masterProcess.getId());
    }

    @Override
    public MasterTopic createMasterTopic(String processId, String topic, String description, MultipartFile biography,
                                         MultipartFile mentorApproval, MultipartFile application, MultipartFile supplement) throws IOException {
        MasterTopic masterTopic = stepService.saveMasterTopic(processId, personService.getAll().stream().sorted(Comparator.comparing(Person::getFullName)).findFirst().get().getId(),
                topic, description, application, mentorApproval, biography, supplement);
        processNextState(processId);
        setUpNewStep(processId);
        return masterTopic;
    }


    private ProcessState firstStepFromPhase(ProcessState processState) {
        if (processState.ordinal() <= INITIAL_SECRETARY_REVIEW.ordinal())
            return DOCUMENT_APPLICATION;
        else if (processState.ordinal() <= SECOND_DRAFT_SECRETARY_REVIEW.ordinal())
            return STUDENT_DRAFT;
        else
            return STUDENT_CHANGES_DRAFT;
    }


    private void validate(String processId, ValidationStatus validationStatus) {
        Step step = stepService.getActiveStep(processId).orElseThrow(() ->
                new RuntimeException(String.format("Step of the processId %s was not found", processId)));
        stepValidationService.changeStepValidationStatus(step.getId(), validationStatus);
        // if go back to master topic - should be added to setUpNewStep
        if (validationStatus.equals(ValidationStatus.REFUSED))
            processService.goToStep(processId, firstStepFromPhase(processService.getProcessState(processId)));
        else
            processNextState(processId);
    }

    @Override
    public void validateStep(String processId, ValidationStatus validationStatus) {
        validate(processId, validationStatus);
        setUpNewStep(processId);
    }

    @Override
    public Master setArchiveNumber(String processId, String archiveNumber) {
        Master master = processService.getProcessMaster(processId);
        return masterService.setArchiveNumber(master.getId(), archiveNumber);
    }

    @Override
    public Attachment uploadDraft(String processId, MultipartFile draft) throws IOException {
        return stepService.saveAttachment(processId, processService.getProcessState(processId).toString(), draft);
    }

    @Override
    public Process confirmUpload(String processId) {
        Process process = processNextState(processId);
        setUpNewStep(processId);
        return process;
    }


}
