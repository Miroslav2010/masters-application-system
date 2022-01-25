package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Process;
import mk.ukim.finki.masterapplicationsystem.domain.*;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ValidationStatus;
import mk.ukim.finki.masterapplicationsystem.service.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

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
        // if method is transactional this would not be the active process state
        ProcessState processState = processService.getProcessState(processId);
        if (EnumSet.of(APPLICATION_FINISHED, FINISHED).contains(processState))
            return;
        if (EnumSet.of(STUDENT_DRAFT, STUDENT_CHANGES_DRAFT).contains(processState))
            setUpDraftStep(processId);
        else
            setUpNewValidation(processId);
    }

//    it's best to create remark when someone leave remark
//    private void assignResponsibleForStep(String processId, Step step) {
//        processStateHelperService.getResponsiblePersonsForStep(processId)
//                .forEach(person -> remarkService.saveNewRemark(person, step));
//    }

    private Process processNextState(String processId) {
        return processService.nextState(processId);
    }

    @Transactional
    @Override
    public Process createMaster(String mentorId, String firstCommitteeId, String secondCommitteeId, String majorId) {
        Student student = (Student) personService.getLoggedInUser();
        permissionService.canPersonCreateMaster(student.getId());
        Professor mentor = findProfessorById(mentorId);
        Professor firstCommittee = findProfessorById(firstCommitteeId);
        Professor secondCommittee = findProfessorById(secondCommitteeId);
        Major major = majorService.findMajorById(majorId);
        Master master = masterService.saveMasterWithAllData(student, mentor, firstCommittee, secondCommittee, major);
        Process masterProcess = processService.save(master);
        return processNextState(masterProcess.getId());
    }

    @Transactional
    @Override
    public MasterTopic createMasterTopic(String processId, String topic, String description, MultipartFile biography,
                                         MultipartFile mentorApproval, MultipartFile application, MultipartFile supplement) throws IOException {
        Person loggedInUser = personService.getLoggedInUser();
        permissionService.canPersonCreateMasterTopic(processId, loggedInUser.getId());
        MasterTopic masterTopic = stepService.saveMasterTopic(processId, loggedInUser.getId(), topic, description,
                application, mentorApproval, biography, supplement);
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

    private void checkIfPersonAlreadyValidated(Step step, String personId) {
        List<StepValidation> stepValidations = stepValidationService.findAllByValidationId(step.getId());
        if (stepValidations.stream().anyMatch(s -> s.getPerson().getId().equals(personId)))
            throw new RuntimeException(String.format("Person with id: %s already validated this step.", personId));
    }

    private boolean allAssignedValidated(Step step) {
        List<StepValidation> stepValidations = stepValidationService.findAllByValidationId(step.getId());
        return stepValidations.stream()
                .filter(s -> s.getValidationStatus() != ValidationStatus.WAITING).count() == (stepValidations.size() - 1);
    }

    private boolean checkIfSomeoneRefused(Step step, ValidationStatus validationStatus) {
        List<StepValidation> stepValidations = stepValidationService.findAllByValidationId(step.getId());
        return stepValidations.stream().anyMatch(s -> s.getValidationStatus() == ValidationStatus.REFUSED)
                || validationStatus == ValidationStatus.REFUSED;
    }

    private void validate(String processId, ValidationStatus validationStatus) {
        Person loggedInUser = personService.getLoggedInUser();
        Step step = stepService.getActiveStep(processId);
        checkIfPersonAlreadyValidated(step, loggedInUser.getId());
        stepValidationService.changeStepValidationStatus(step.getId(), validationStatus);
        if (!allAssignedValidated(step))
            return;
        if (checkIfSomeoneRefused(step, validationStatus)) {
            processService.goToStep(processId, firstStepFromPhase(processService.getProcessState(processId)));
            setUpNewStep(processId);
        }
        else
            processNextState(processId);
    }

    @Transactional
    @Override
    public void validateStep(String processId, ValidationStatus validationStatus) {
        Person loggedInUser = personService.getLoggedInUser();
        permissionService.canPersonValidateMaster(processId, loggedInUser.getId());
        validate(processId, validationStatus);
        setUpNewStep(processId);
    }

    @Transactional
    @Override
    public Master setArchiveNumber(String processId, String archiveNumber) {
        Master master = processService.getProcessMaster(processId);
        return masterService.setArchiveNumber(master.getId(), archiveNumber);
    }

    @Transactional
    @Override
    public Attachment uploadDraft(String processId, MultipartFile draft) throws IOException {
        Person loggedInUser = personService.getLoggedInUser();
        permissionService.canPersonUploadAttachment(processId, loggedInUser.getId());
        return stepService.saveAttachment(processId, processService.getProcessState(processId).toString(), draft);
    }

    @Transactional
    @Override
    public Process confirmUpload(String processId) {
        Person loggedInUser = personService.getLoggedInUser();
        permissionService.canPersonUploadAttachment(processId, loggedInUser.getId());
        Process process = processNextState(processId);
        setUpNewStep(processId);
        return process;
    }


}
