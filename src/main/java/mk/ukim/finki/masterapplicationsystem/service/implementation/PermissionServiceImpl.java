package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Master;
import mk.ukim.finki.masterapplicationsystem.domain.Person;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.Role;
import mk.ukim.finki.masterapplicationsystem.domain.permissions.Permission;
import mk.ukim.finki.masterapplicationsystem.repository.PermissionRepository;
import mk.ukim.finki.masterapplicationsystem.service.MasterService;
import mk.ukim.finki.masterapplicationsystem.service.PermissionService;
import mk.ukim.finki.masterapplicationsystem.service.PersonService;
import mk.ukim.finki.masterapplicationsystem.service.ProcessService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState.*;

@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final MasterService masterService;
    private final ProcessService processService;
    private final PersonService personService;

    public PermissionServiceImpl(PermissionRepository permissionRepository, MasterService masterService, ProcessService processService, PersonService personService) {
        this.permissionRepository = permissionRepository;
        this.masterService = masterService;
        this.processService = processService;
        this.personService = personService;
    }

    private List<ProcessState> getAvailableStatesForRole(Role role) {
        if (role.equals(Role.STUDENT))
            return new ArrayList<>(EnumSet.of(APPLICATION, DOCUMENT_APPLICATION, STUDENT_DRAFT, STUDENT_CHANGES_DRAFT));
        if (role.equals(Role.PROFESSOR))
            return new ArrayList<>(EnumSet.of(INITIAL_MENTOR_REVIEW, DRAFT_MENTOR_REVIEW, MENTOR_REPORT));
        if (role.equals(Role.COMMITTEE))
            return new ArrayList<>(EnumSet.of(DRAFT_COMMITTEE_REVIEW, REPORT_REVIEW));
        if (role.equals(Role.NNK))
            return new ArrayList<>(EnumSet.of(INITIAL_NNK_REVIEW, DRAFT_NNK_REVIEW));
        if (role.equals(Role.STUDENT_SERVICE))
            return new ArrayList<>(EnumSet.of(STUDENT_SERVICE_REVIEW, REPORT_STUDENT_SERVICE));
        if (role.equals(Role.SECRETARY))
            return Arrays.asList(ProcessState.values());
        return Collections.emptyList();
    }

    private Permission findByProcessIdAndPersonId(String processId, String personId) {
        return permissionRepository.findByProcessIdAndPersonId(processId, personId)
                .orElseThrow(() -> new RuntimeException(String.format("Person with id: %s don't have permissions for process with id: %s", personId, processId)));
    }

//    @Override
//    public Permission addMentorPermissions(String processId, String mentorId) {
//        Permission permission = new Permission(processId, mentorId, getAvailableStatesForRole(Role.PROFESSOR));
//        return permissionRepository.save(permission);
//    }
//
//    @Override
//    public Permission addStudentPermissions(String processId, String studentId) {
//        Permission permission = new Permission(processId, studentId, getAvailableStatesForRole(Role.STUDENT));
//        return permissionRepository.save(permission);
//    }
//
//    @Override
//    public Permission addCommitteePermissions(String processId, String committeeId) {
//        Permission permission = new Permission(processId, committeeId, getAvailableStatesForRole(Role.COMMITTEE));
//        return permissionRepository.save(permission);
//    }

    @Override
    public void canPersonTakeAction(String processId, String personId, String permissionType) {
        Permission permission = findByProcessIdAndPersonId(processId, personId);
        if (!permission.getPermissionTypes().stream().map(Enum::toString).collect(Collectors.toList()).contains(permissionType))
            throw new RuntimeException(String.format("Person with id: %s don't have permission %s for process with id: %s",
                    personId, permissionType, processId));
    }

    @Override
    public void canPersonCreateMaster(String personId) {
        //when a student can create new master (if one already exist) and can other authority create master
        checkIfPersonHaveRole(personId, Role.STUDENT);
        Boolean studentHaveActiveMaster = masterService.doesStudentHaveActiveMaster(personId);
        if (studentHaveActiveMaster)
            throw new RuntimeException(String.format("Student with id: %s has already an active master.", personId));
    }

    private void checkIfPersonHaveRole(String personId, Role role) {
        Person person = personService.getPerson(personId);
        if (!person.getRoles().contains(role))
            throw new RuntimeException(
                    String.format("Person with id: %s does not have the role %s, so it cannot affect this step.", personId, role.toString()));
    }

    private void checkIfPersonIsAssignedOnMasterAsRole(String processId, String personId, Role role) {
        Master master = processService.getProcessMaster(processId);
        String masterPersonId = "";
        if (role.equals(Role.STUDENT))
            masterPersonId = master.getStudent().getId();
        if (role.equals(Role.PROFESSOR))
            masterPersonId = master.getMentor().getId();
        if (role.equals(Role.COMMITTEE)) {
            if (master.getCommitteeFirst().getId().equals(personId))
                masterPersonId = personId;
            else
                masterPersonId = master.getCommitteeSecond().getId();
        }
        if (!masterPersonId.equals(personId))
            throw new RuntimeException(
                    String.format("Person with id: %s is not assigned on master with id: %s", personId, master.getId()));
    }

    private void checkIfProcessStateIsDifferentFromTheActionStep(ProcessState currentState, ProcessState actionState) {
        if (!currentState.equals(actionState))
            throw new RuntimeException(
                    String.format("The master is not in the %s step, so changes are not allowed.", actionState));
    }

    @Override
    public void canPersonCreateMasterTopic(String processId, String personId) {
        checkIfPersonHaveRole(personId, Role.STUDENT);
        checkIfPersonIsAssignedOnMasterAsRole(processId, personId, Role.STUDENT);
        ProcessState processState = processService.getProcessState(processId);
        checkIfProcessStateIsDifferentFromTheActionStep(processState, ProcessState.DOCUMENT_APPLICATION);
    }

    @Override
    public void canPersonValidateMaster(String processId, String personId) {
        ProcessState processState = processService.getProcessState(processId);
        if (EnumSet.of(INITIAL_MENTOR_REVIEW, DRAFT_MENTOR_REVIEW, MENTOR_REPORT).contains(processState)) {
            checkIfPersonHaveRole(personId, Role.PROFESSOR);
            checkIfPersonIsAssignedOnMasterAsRole(processId, personId, Role.PROFESSOR);
        } else if (EnumSet.of(STUDENT_SERVICE_REVIEW, REPORT_STUDENT_SERVICE).contains(processState)) {
            checkIfPersonHaveRole(personId, Role.STUDENT_SERVICE);
        } else if (EnumSet.of(INITIAL_NNK_REVIEW, DRAFT_NNK_REVIEW).contains(processState)) {
            checkIfPersonHaveRole(personId, Role.NNK);
        } else if (EnumSet.of(INITIAL_SECRETARY_REVIEW, DRAFT_SECRETARY_REVIEW, SECOND_DRAFT_SECRETARY_REVIEW, REPORT_SECRETARY_REVIEW).contains(processState)) {
            checkIfPersonHaveRole(personId, Role.SECRETARY);
        } else if (EnumSet.of(DRAFT_COMMITTEE_REVIEW, REPORT_REVIEW).contains(processState)) {
            checkIfPersonHaveRole(personId, Role.PROFESSOR);
            checkIfPersonIsAssignedOnMasterAsRole(processId, personId, Role.COMMITTEE);
        } else
            throw new RuntimeException(String.format("Tried to validate in step %s that is not validation.", processState));
    }

    @Override
    public void canPersonCancelRevisionLoop(String processId, String personId) {
        ProcessState processState = processService.getProcessState(processId);
        if (EnumSet.of(DRAFT_COMMITTEE_REVIEW, STUDENT_CHANGES_DRAFT).contains(processState)) {
            checkIfPersonHaveRole(personId, Role.PROFESSOR);
            checkIfPersonIsAssignedOnMasterAsRole(processId, personId, Role.PROFESSOR);
        }
        else
            throw new RuntimeException(String.format("Tried to cancel the revision in step %s that is not revision or change draft.", processState));
    }

    @Override
    public void canPersonUploadAttachment(String processId, String personId) {
        ProcessState processState = processService.getProcessState(processId);
        if (EnumSet.of(STUDENT_DRAFT, STUDENT_CHANGES_DRAFT).contains(processState)) {
            checkIfPersonHaveRole(personId, Role.STUDENT);
            checkIfPersonIsAssignedOnMasterAsRole(processId, personId, Role.STUDENT);
        } else if (processState.equals(MENTOR_REPORT)) {
            checkIfPersonHaveRole(personId, Role.PROFESSOR);
            checkIfPersonIsAssignedOnMasterAsRole(processId, personId, Role.PROFESSOR);
        }
        else
            throw new RuntimeException(String.format("Tried to upload document in step %s that is not attachment.", processState));
    }

    @Override
    public void canPersonWriteRemark(String processId, String personId) {

    }


}
