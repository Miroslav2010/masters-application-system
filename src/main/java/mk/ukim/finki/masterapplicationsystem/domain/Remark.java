package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class Remark {

    @Id
    private final String id = UUID.randomUUID().toString();

    @ManyToOne
    private Person person;

    @ManyToOne
    private Step step;

    private String remark;

    private OffsetDateTime dateTime;

    public Remark(Person person, Step step, String remark, OffsetDateTime dateTime) {
        this.person = person;
        this.step = step;
        this.remark = remark;
        this.dateTime = dateTime;
    }
}
