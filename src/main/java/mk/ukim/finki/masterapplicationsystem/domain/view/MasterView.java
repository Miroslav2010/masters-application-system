package mk.ukim.finki.masterapplicationsystem.domain.view;

import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@Subselect("select p.id, st.id as student_id, me.id as mentor_id, fc.id as first_committee_id, sc.id as second_committee_id," +
        " st.full_name || ', ' || st.index as student, me.full_name as mentor, p.process_state as step, ss.created as last_modified from process p " +
        "join master m on p.master_id = m.id " +
        "join person st on m.student_id = st.id " +
        "join person me on m.mentor_id = me.id " +
        "join person fc on m.committee_first_id = fc.id " +
        "join person sc on m.committee_second_id = sc.id " +
        "join (select distinct on (process_id) " +
        "process_id, created, name, order_number " +
        "FROM step " +
        "ORDER BY process_id, order_number DESC) as ss on ss.process_id = p.id")
@Immutable
@Getter
public class MasterView {

    @Id
    private String id;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "mentor_id")
    private String mentorId;

    @Column(name = "first_committee_id")
    private String firstCommitteeId;

    @Column(name = "second_committee_id")
    private String secondCommitteeId;

    private String student;

    private String mentor;

    private String step;

    @Column(name = "last_modified")
    private Date lastModified;

}


