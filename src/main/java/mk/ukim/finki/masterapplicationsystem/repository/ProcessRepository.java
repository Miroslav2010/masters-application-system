package mk.ukim.finki.masterapplicationsystem.repository;

import mk.ukim.finki.masterapplicationsystem.domain.Process;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProcessRepository extends JpaRepository<Process, String> {

    Optional<Process> findByMasterId(String masterId);

    List<Process> findAllByMaster_StudentIdOrMaster_MentorIdOrMaster_CommitteeFirstIdOrMaster_CommitteeSecondId
            (String studentId, String mentorId, String firstCommitteeId, String secondCommitteeId);

}
