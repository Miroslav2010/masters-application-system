package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class Master {

    @Id
    private final String id = UUID.randomUUID().toString();

    private OffsetDateTime dateTime;

    private OffsetDateTime finishedDate;

    private OffsetDateTime masterDefenseDate;

    private String archiveNumber;

    @ManyToOne
    private Major major;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Professor mentor;

    @ManyToOne
    private Professor committeeFirst;

    @ManyToOne
    private Professor committeeSecond;

    public Master(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDateTime(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setFinishedDate(OffsetDateTime finishedDate) {
        this.finishedDate = finishedDate;
    }

    public void setMasterDefenseDate(OffsetDateTime masterDefenseDate) {
        this.masterDefenseDate = masterDefenseDate;
    }

    public void setArchiveNumber(String archiveNumber) {
        this.archiveNumber = archiveNumber;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public void setMentor(Professor mentor) {
        this.mentor = mentor;
    }

    public void setCommitteeFirst(Professor committeeFirst) {
        this.committeeFirst = committeeFirst;
    }

    public void setCommitteeSecond(Professor committeeSecond) {
        this.committeeSecond = committeeSecond;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
