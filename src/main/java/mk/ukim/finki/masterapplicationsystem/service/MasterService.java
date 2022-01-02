package mk.ukim.finki.masterapplicationsystem.service;

import mk.ukim.finki.masterapplicationsystem.domain.Major;
import mk.ukim.finki.masterapplicationsystem.domain.Master;
import mk.ukim.finki.masterapplicationsystem.domain.Professor;
import mk.ukim.finki.masterapplicationsystem.domain.Student;

import java.time.OffsetDateTime;
import java.util.List;

public interface MasterService {

    List<Master> findAllMasters();

    Master findMasterById(String id);

    OffsetDateTime getMasterCreatedDateTime(String id);

    OffsetDateTime getMasterFinishedDateTime(String id);

    OffsetDateTime getMasterDefencedDateTime(String id);

    Master saveMaster(OffsetDateTime dateTimeCreated, Student student);

    Master saveMasterWithAllData(String id, Student student, Professor mentor, Professor firstCommittee, Professor secondCommittee, Major major);

    Boolean isMasterFinished(String id);

    Boolean isMasterDefenced(String id);

    Master markMasterAsFinished(String id, OffsetDateTime finishedDateTime);

    Master marMasterAsDefenced(String id, OffsetDateTime defencedDateTime);

    Master setArchiveNumber(String id, String archiveNumber);

    Master setMajor(String id, Major major);

    Master assignMentor(String id, Professor mentor);

    Master assignMembers(String id, Professor firstCommittee, Professor secondCommittee);

    Master assignOwner(String id, Student student);
}
