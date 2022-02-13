package mk.ukim.finki.masterapplicationsystem.repository;

import mk.ukim.finki.masterapplicationsystem.domain.Process;
import mk.ukim.finki.masterapplicationsystem.domain.dto.response.MasterPreviewView;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProcessRepository extends JpaRepository<Process, String> {

    Optional<Process> findByMasterId(String masterId);

    List<Process> findAllByMaster_StudentIdOrMaster_MentorIdOrMaster_CommitteeFirstIdOrMaster_CommitteeSecondId
            (String studentId, String mentorId, String firstCommitteeId, String secondCommitteeId);

    List<Process> findAllByProcessStateIn(List<ProcessState> processStates);

    @Query(value = "select p.id, st.full_name || ', ' || st.index as student, me.full_name as mentor, p.process_state as step, ss.created as lastmodified from process p " +
            "join master m on p.master_id = m.id " +
            "join person st on m.student_id = st.id " +
            "join person me on m.mentor_id = me.id " +
            "join person fc on m.committee_first_id = fc.id " +
            "join person sc on m.committee_second_id = sc.id " +
            "join (select distinct on (process_id) " +
            "process_id, created, name, order_number " +
            "FROM step " +
            "ORDER BY process_id, order_number DESC) as ss on ss.process_id = p.id " +
            "where (student_id like ?1 or mentor_id like ?1 or committee_first_id like ?1 or committee_second_id like ?1) " +
            "and (st.full_name ilike ?2 or st.index ilike ?2 or me.full_name ilike ?2 or p.process_state ilike ?2) order by lastmodified desc",
            countQuery = "select count(*) from process p " +
                    "join master m on p.master_id = m.id " +
                    "join person st on m.student_id = st.id " +
                    "join person me on m.mentor_id = me.id " +
                    "join person fc on m.committee_first_id = fc.id " +
                    "join person sc on m.committee_second_id = sc.id " +
                    "join (select distinct on (process_id) " +
                    "process_id, created, name, order_number " +
                    "FROM step " +
                    "ORDER BY process_id, order_number DESC) as ss on ss.process_id = p.id " +
                    "where (student_id like ?1 or mentor_id like ?1 or committee_first_id like ?1 or committee_second_id like ?1) " +
                    "and (st.full_name ilike ?2 or st.index ilike ?2 or me.full_name ilike ?2 or p.process_state ilike ?2)",
            nativeQuery = true)
//    @Query(value = "select s from Step s join s.process p join p.master m ")
    Page<MasterPreviewView> findAllMastersFromProcess(@Param("personId") String personId, @Param("filter") String filter, Pageable pageable);
}
