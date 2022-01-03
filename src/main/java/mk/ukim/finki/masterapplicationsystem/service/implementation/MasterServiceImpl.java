package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Major;
import mk.ukim.finki.masterapplicationsystem.domain.Master;
import mk.ukim.finki.masterapplicationsystem.domain.Professor;
import mk.ukim.finki.masterapplicationsystem.domain.Student;
import mk.ukim.finki.masterapplicationsystem.repository.MasterRepository;
import mk.ukim.finki.masterapplicationsystem.service.MasterService;

import java.time.OffsetDateTime;
import java.util.List;

public class MasterServiceImpl implements MasterService {
    private MasterRepository masterRepository;

    public MasterServiceImpl(MasterRepository masterRepository) {
        this.masterRepository = masterRepository;
    }

    @Override
    public List<Master> findAllMasters() {
        return masterRepository.findAll();
    }

    @Override
    public Master findMasterById(String id) {
        return masterRepository.findById(id).orElseThrow(() -> new RuntimeException("Master with id: " + id + " was not found."));
    }

    @Override
    public OffsetDateTime getMasterCreatedDateTime(String id) {
        return findMasterById(id).getDateTime();
    }

    @Override
    public OffsetDateTime getMasterFinishedDateTime(String id) {
        return findMasterById(id).getFinishedDate();
    }

    @Override
    public OffsetDateTime getMasterDefencedDateTime(String id) {
        return findMasterById(id).getMasterDefenseDate();
    }

    @Override
    public Master saveMaster(OffsetDateTime dateTimeCreated, Student student) {
        Master master = new Master(dateTimeCreated);
        master.setStudent(student);
        return masterRepository.save(master);
    }

    @Override
    public Master saveMasterWithAllData(String id, Student student, Professor mentor, Professor firstCommittee, Professor secondCommittee, Major major) {
        Master master = findMasterById(id);
        master.setStudent(student);
        master.setMentor(mentor);
        master.setCommitteeFirst(firstCommittee);
        master.setCommitteeSecond(secondCommittee);
        master.setMajor(major);
        return masterRepository.save(master);
    }

    @Override
    public Boolean isMasterFinished(String id) {
        return getMasterFinishedDateTime(id) != null;
    }

    @Override
    public Boolean isMasterDefenced(String id) {
        return getMasterDefencedDateTime(id) != null;
    }

    @Override
    public Master markMasterAsFinished(String id, OffsetDateTime finishedDateTime) {
        Master master = findMasterById(id);
        master.setFinishedDate(finishedDateTime);
        return masterRepository.save(master);
    }

    @Override
    public Master marMasterAsDefenced(String id, OffsetDateTime defencedDateTime) {
        Master master = findMasterById(id);
        master.setMasterDefenseDate(defencedDateTime);
        return masterRepository.save(master);
    }

    @Override
    public Master setArchiveNumber(String id, String archiveNumber) {
        Master master = findMasterById(id);
        master.setArchiveNumber(archiveNumber);
        return masterRepository.save(master);
    }

    @Override
    public Master setMajor(String id, Major major) {
        Master master = findMasterById(id);
        master.setMajor(major);
        return masterRepository.save(master);
    }

    @Override
    public Master assignMentor(String id, Professor mentor) {
        Master master = findMasterById(id);
        master.setMentor(mentor);
        return masterRepository.save(master);
    }

    @Override
    public Master assignMembers(String id, Professor firstCommittee, Professor secondCommittee) {
        Master master = findMasterById(id);
        master.setCommitteeFirst(firstCommittee);
        master.setCommitteeSecond(secondCommittee);
        return masterRepository.save(master);
    }

    @Override
    public Master assignOwner(String id, Student student) {
        Master master = findMasterById(id);
        master.setStudent(student);
        return masterRepository.save(master);
    }
}
