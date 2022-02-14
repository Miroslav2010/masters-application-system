package mk.ukim.finki.masterapplicationsystem.repository;


import mk.ukim.finki.masterapplicationsystem.domain.view.MasterView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface MasterViewRepository extends ReadOnlyRepository<MasterView, String> {

    @Query("SELECT v FROM MasterView v where (v.studentId like ?1 or v.mentorId like ?1 or v.firstCommitteeId like ?1 or v.secondCommitteeId like ?1) " +
            "and (lower(v.student) like ?2 or lower(v.mentor) like ?2)")
    Page<MasterView> findAllPageable(String personId, String filter, Pageable pageable);

}
