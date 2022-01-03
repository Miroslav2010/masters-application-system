package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
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
}
