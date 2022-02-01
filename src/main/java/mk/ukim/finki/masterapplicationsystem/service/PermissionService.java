package mk.ukim.finki.masterapplicationsystem.service;

import mk.ukim.finki.masterapplicationsystem.domain.permissions.Permission;

public interface PermissionService {

//    Permission addMentorPermissions(String processId, String mentorId);
//
//    Permission addStudentPermissions(String processId, String studentId);
//
//    Permission addCommitteePermissions(String processId, String committeeId);

    void canPersonTakeAction(String processId, String personId, String permissionType);

    void canPersonCreateMaster(String personId);

    void canPersonCreateMasterTopic(String processId, String personId);

    void canPersonValidateMaster(String processId, String personId);

    void canPersonCancelRevisionLoop(String processId, String personId);

    void canPersonUploadAttachment(String processId, String personId);

    void canPersonWriteRemark(String processId, String personId);
}