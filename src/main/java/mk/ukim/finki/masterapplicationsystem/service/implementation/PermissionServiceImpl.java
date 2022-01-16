package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.enumeration.Role;
import mk.ukim.finki.masterapplicationsystem.domain.permissions.Permission;
import mk.ukim.finki.masterapplicationsystem.domain.permissions.PermissionType;
import mk.ukim.finki.masterapplicationsystem.repository.PermissionRepository;
import mk.ukim.finki.masterapplicationsystem.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static mk.ukim.finki.masterapplicationsystem.domain.permissions.PermissionType.*;

@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    private List<PermissionType> getPermissionsForType(Role role) {
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
            return Arrays.asList(PermissionType.values());
        return Collections.emptyList();
    }

    private Permission findByProcessIdAndPersonId(String processId, String personId) {
        return permissionRepository.findByProcessIdAndPersonId(processId, personId)
                .orElseThrow(() -> new RuntimeException(String.format("Person with id: %s don't have permissions for process with id: %s", personId, processId)));
    }

    @Override
    public Permission addMentorPermissions(String processId, String mentorId) {
        Permission permission = new Permission(processId, mentorId, getPermissionsForType(Role.PROFESSOR));
        return permissionRepository.save(permission);
    }

    @Override
    public Permission addStudentPermissions(String processId, String studentId) {
        Permission permission = new Permission(processId, studentId, getPermissionsForType(Role.STUDENT));
        return permissionRepository.save(permission);
    }

    @Override
    public Permission addCommitteePermissions(String processId, String committeeId) {
        Permission permission = new Permission(processId, committeeId, getPermissionsForType(Role.COMMITTEE));
        return permissionRepository.save(permission);
    }

    @Override
    public void canPersonTakeAction(String processId, String personId, String permissionType) {
        Permission permission = findByProcessIdAndPersonId(processId, personId);
        if (!permission.getPermissionTypes().stream().map(Enum::toString).collect(Collectors.toList()).contains(permissionType))
            throw new RuntimeException(String.format("Person with id: %s don't have permission %s for process with id: %s",
                    personId, permissionType, processId));
    }
}
