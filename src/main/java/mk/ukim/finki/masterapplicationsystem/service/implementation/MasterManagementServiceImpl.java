package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Process;
import mk.ukim.finki.masterapplicationsystem.domain.*;
import mk.ukim.finki.masterapplicationsystem.domain.dto.response.ValidationResponseDTO;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.Role;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ValidationStatus;
import mk.ukim.finki.masterapplicationsystem.service.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

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
    private final DocumentService documentService;

    public MasterManagementServiceImpl(MasterService masterService, ProcessService processService, StepService stepService,
                                       PersonService personService, MajorService majorService, StepValidationService stepValidationService, ProcessStateHelperService processStateHelperService, PermissionService permissionService, DocumentService documentService) {
        this.masterService = masterService;
        this.processService = processService;
        this.stepService = stepService;
        this.personService = personService;
        this.majorService = majorService;
        this.stepValidationService = stepValidationService;
        this.permissionService = permissionService;
        this.documentService = documentService;
    }

    private Professor findProfessorById(String id) {
        return (Professor) personService.getPerson(id);
    }

    private void setUpNewValidation(String processId) {
        // for student service, secretary and nnk we do not assign people
        stepService.saveValidation(processId);
    }

    private void setUpDraftStep(String processId) {
        // just temporary
        try {
            stepService.initializeAttachment(processId);
        }
        catch (Exception e) {

        }
    }

    private void setUpNewStep(String processId) {
        // if method is transactional this would not be the active process state
        ProcessState processState = processService.getProcessState(processId);
        if (EnumSet.of(APPLICATION_FINISHED, FINISHED).contains(processState))
            return;
        if (EnumSet.of(STUDENT_DRAFT, STUDENT_CHANGES_DRAFT, MENTOR_REPORT).contains(processState))
            setUpDraftStep(processId);
        else if (processState == DOCUMENT_APPLICATION) {
            try {
                setUpNewMasterTopic(processId);
            }
            catch (Exception e){

            }
        }
        else
            setUpNewValidation(processId);
    }

    private void setUpNewMasterTopic(String processId) throws IOException {
        MasterTopic masterTopic = stepService.getMasterTopicFromProcess(processId);
        Master master = processService.getProcessMaster(processId);
        // TODO: get files from previous master topic
        MockMultipartFile file = new MockMultipartFile("draft", "draft.pdf", "application/pdf", "app files".getBytes());
        MasterTopic newMasterTopic = stepService.saveMasterTopic(processId, master.getStudent().getId(), masterTopic.getTopic(),
                masterTopic.getDescription(), file, file, file, file);
    }

//    it's best to create remark when someone leave remark
//    private void assignResponsibleForStep(String processId, Step step) {
//        processStateHelperService.getResponsiblePersonsForStep(processId)
//                .forEach(person -> remarkService.saveNewRemark(person, step));
//    }

    private Process processNextState(String processId) {
        Process process = processService.findProcessById(processId);
        ProcessState processState = process.getProcessState();
        if (processState == STUDENT_CHANGES_DRAFT) {
            return processService.setState(process, DRAFT_COMMITTEE_REVIEW.ordinal());
        }
        else if (processState == DRAFT_COMMITTEE_REVIEW) {
            Step step = stepService.getActiveStep(processId);
            if(!checkIfSomeoneRefused(step))
                return processService.setState(process, MENTOR_REPORT.ordinal());
        }
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
        List<StepValidation> stepValidations = stepValidationService.findAllByValidationId(step.getId()).stream()
                .filter(s -> s.getValidationStatus() != ValidationStatus.WAITING).collect(Collectors.toList());
        if (stepValidations.stream().anyMatch(s -> s.getPerson().getId().equals(personId)))
            throw new RuntimeException(String.format("Person with id: %s already validated this step.", personId));
    }

    private boolean allAssignedValidated(Step step) {
        return stepValidationService.findAllByValidationId(step.getId()).stream()
                .allMatch(s -> s.getValidationStatus() != ValidationStatus.WAITING);
    }

    private boolean checkIfSomeoneRefused(Step step) {
        return stepValidationService.findAllByValidationId(step.getId()).stream()
                .anyMatch(s -> s.getValidationStatus() == ValidationStatus.REFUSED);
    }

    private void validate(String processId, ValidationStatus validationStatus) {
        Person loggedInUser = personService.getLoggedInUser();
        Step step = stepService.getActiveStep(processId);
        checkIfPersonAlreadyValidated(step, loggedInUser.getId());
        stepValidationService.changeStepValidationStatus(step, loggedInUser, validationStatus);
        if (!allAssignedValidated(step))
            return;
        if (checkIfSomeoneRefused(step)) {
            processService.goToStep(processId, firstStepFromPhase(processService.getProcessState(processId)));
        }
        else
            processNextState(processId);
        setUpNewStep(processId);
    }

    @Transactional
    @Override
    public void validateStep(String processId, ValidationStatus validationStatus) {
        Person loggedInUser = personService.getLoggedInUser();
        permissionService.canPersonValidateMaster(processId, loggedInUser.getId());
        validate(processId, validationStatus);
    }

    @Transactional
    @Override
    public Master setArchiveNumber(String processId, String archiveNumber) {
        Master master = processService.getProcessMaster(processId);
        return masterService.setArchiveNumber(master.getId(), archiveNumber);
    }

    private void isCancelingRevisionLoop(String processId, Person person) {
        if (!person.getRoles().contains(Role.PROFESSOR))
            return;
        ProcessState processState = processService.getProcessState(processId);
        if (EnumSet.of(STUDENT_CHANGES_DRAFT, DRAFT_COMMITTEE_REVIEW).contains(processState))
            cancelRevisionLoop(processId);
    }

    @Transactional
    @Override
    public Attachment uploadDraft(String processId, MultipartFile draft) throws IOException {
        Person loggedInUser = personService.getLoggedInUser();
        isCancelingRevisionLoop(processId, loggedInUser);
        permissionService.canPersonUploadAttachment(processId, loggedInUser.getId());
        ProcessState processState = processService.getProcessState(processId);
        return stepService.editAttachment(processId, processState, loggedInUser.getId(), processState.toString(), draft);
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

    @Override
    public Process cancelRevisionLoop(String processId) {
        Person loggedInUser = personService.getLoggedInUser();
        permissionService.canPersonCancelRevisionLoop(processId, loggedInUser.getId());
        Process process = processService.findProcessById(processId);
        process = processService.setState(process, MENTOR_REPORT.ordinal());
        setUpNewStep(processId);
        return process;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public ValidationResponseDTO getValidationDetails(String processId) {
//        Person loggedInUser = personService.getLoggedInUser();
//        permissionService.canPersonValidateMaster(processId, "b15a901f-5c68-4f97-ba63-28180aa927fa");
        ProcessState state = processService.getProcessState(processId);
        Master master = processService.getProcessMaster(processId);
        List<String> documentLocations = getDocumentLocations(processId);
        return new ValidationResponseDTO(state.label, master.getStudent().getFullName(), documentLocations);
    }

    private List<String> getDocumentLocations(String processId) {
//        ProcessState processState = processService.getProcessState(processId);
        ProcessState processState = DOCUMENT_APPLICATION;
        ProcessState draftState = firstStepFromPhase(processState);
        List<String> documentLocations = new ArrayList<>();
        if (draftState == DOCUMENT_APPLICATION) {
            MasterTopic masterTopic = stepService.getMasterTopicFromProcess(processId);
            documentLocations.add(masterTopic.getApplication().getLocation());
            documentLocations.add(masterTopic.getBiography().getLocation());
            documentLocations.add(masterTopic.getMentorApproval().getLocation());
            documentLocations.add(masterTopic.getSupplement().getLocation());
        }
        else if (draftState == STUDENT_DRAFT) {
            Attachment attachment = stepService.getAttachmentFromProcess(processId, STUDENT_DRAFT.toString());
            documentLocations.add(attachment.getDocument().getLocation());
        }
        else if (draftState == STUDENT_CHANGES_DRAFT) {
            // when should be STUDENT_CHANGES_DRAFT and when MENTOR_REVIEW
            Attachment attachment = stepService.getAttachmentFromProcess(processId, STUDENT_CHANGES_DRAFT.toString());
            documentLocations.add(attachment.getDocument().getLocation());
        }
        return documentLocations;
    }


}
