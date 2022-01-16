package mk.ukim.finki.masterapplicationsystem.service;

import mk.ukim.finki.masterapplicationsystem.domain.permissions.Permission;

public interface PermissionService {

    Permission addMentorPermissions(String processId, String mentorId);

    Permission addStudentPermissions(String processId, String studentId);

    Permission addCommitteePermissions(String processId, String committeeId);

    void canPersonTakeAction(String processId, String personId, String permissionType);
}
