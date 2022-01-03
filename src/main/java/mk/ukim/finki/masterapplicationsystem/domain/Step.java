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
public class Step {

    @Id
    private final String id = UUID.randomUUID().toString();

    private OffsetDateTime created;
    private OffsetDateTime closed;
    private int orderNumber;
    private String name;

    @ManyToOne
    private Process process;

    public Step(int orderNumber, String name) {
        this.created = OffsetDateTime.now();
        this.closed = null;
        this.orderNumber = orderNumber;
        this.name = name;
    }

    public void setClosed(OffsetDateTime closed) {
        this.closed = closed;
    }
}
