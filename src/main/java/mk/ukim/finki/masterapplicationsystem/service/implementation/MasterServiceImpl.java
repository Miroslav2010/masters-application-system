package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Major;
import mk.ukim.finki.masterapplicationsystem.domain.Master;
import mk.ukim.finki.masterapplicationsystem.domain.Professor;
import mk.ukim.finki.masterapplicationsystem.domain.Student;
import mk.ukim.finki.masterapplicationsystem.domain.view.MasterView;
import mk.ukim.finki.masterapplicationsystem.repository.MasterRepository;
import mk.ukim.finki.masterapplicationsystem.repository.MasterViewRepository;
import mk.ukim.finki.masterapplicationsystem.service.MasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class MasterServiceImpl implements MasterService {
    private final Logger logger = LoggerFactory.getLogger(MasterServiceImpl.class);
    private final MasterRepository masterRepository;
    private final MasterViewRepository masterViewRepository;

    public MasterServiceImpl(MasterRepository masterRepository, MasterViewRepository masterViewRepository) {
        this.masterRepository = masterRepository;
        this.masterViewRepository = masterViewRepository;
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
    public Master saveMaster(Student student) {
        Master master = new Master();
        master.setStudent(student);
        master = masterRepository.save(master);
        logger.info("Saved new Master by {}", student.getFullName());
        return master;
    }

    @Override
    public Master saveMasterWithAllData(Student student, Professor mentor, Professor firstCommittee, Professor secondCommittee, Major major) {
        Master master = new Master();
        master.setStudent(student);
        master.setMentor(mentor);
        master.setCommitteeFirst(firstCommittee);
        master.setCommitteeSecond(secondCommittee);
        master.setMajor(major);
        master = masterRepository.save(master);
        logger.debug(String.format("Saved new master with all data by %s", student.getFullName()));
        return master;
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
    public Boolean doesStudentHaveActiveMaster(String personId) {
        List<Master> masters = masterRepository.findAllByStudentId(personId);
        return masters.stream().anyMatch(s -> !isMasterFinished(s.getId()));
    }

    @Override
    public Master markMasterAsFinished(String id, OffsetDateTime finishedDateTime) {
        Master master = findMasterById(id);
        master.setFinishedDate(finishedDateTime);
        master = masterRepository.save(master);
        logger.debug(String.format("Master with id: %s marked as finished", id));
        return master;
    }

    @Override
    public Master marMasterAsDefenced(String id, OffsetDateTime defencedDateTime) {
        Master master = findMasterById(id);
        master.setMasterDefenseDate(defencedDateTime);
        logger.debug(String.format("Master with id: %s marked as defended", id));
        return masterRepository.save(master);
    }

    @Override
    public Master setArchiveNumber(String id, String archiveNumber) {
        Master master = findMasterById(id);
        master.setArchiveNumber(archiveNumber);
        master = masterRepository.save(master);
        logger.debug(String.format("Master with id: %s has archive number %s", id, archiveNumber));
        return master;
    }

    @Override
    public Page<MasterView> findAllMastersPageable(String personId, String filter, Pageable pageable) {
        return masterViewRepository.findAllPageable(personId, "%" + filter.toLowerCase() + "%", pageable);
    }

    @Override
    public Master setMajor(String id, Major major) {
        Master master = findMasterById(id);
        master.setMajor(major);
        master = masterRepository.save(master);
        return master;
    }

    @Override
    public Master assignMentor(String id, Professor mentor) {
        Master master = findMasterById(id);
        master.setMentor(mentor);
        master = masterRepository.save(master);
        logger.debug(String.format("Mentor %s set for master with id: %s", mentor.getFullName(), id));
        return master;
    }

    @Override
    public Master assignMembers(String id, Professor firstCommittee, Professor secondCommittee) {
        Master master = findMasterById(id);
        master.setCommitteeFirst(firstCommittee);
        master.setCommitteeSecond(secondCommittee);
        master = masterRepository.save(master);
        logger.debug(String.format("Assigned members %s and %s to master with id: %s", firstCommittee.getFullName(), secondCommittee.getFullName(), id));
        return master;
    }

    @Override
    public Master assignOwner(String id, Student student) {
        Master master = findMasterById(id);
        master.setStudent(student);
        return masterRepository.save(master);
    }
}
