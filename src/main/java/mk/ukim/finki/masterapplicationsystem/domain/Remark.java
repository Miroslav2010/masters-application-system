package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
public class Remark {

    @Id
    private final String id = UUID.randomUUID().toString();

    @ManyToOne
    private Person person;

    @ManyToOne
    private Step step;

    private String remark;

    private OffsetDateTime dateTime;

    public Remark(Person person, Step step) {
        this.person = person;
        this.step = step;
    }

    public Remark() {

    }
}
