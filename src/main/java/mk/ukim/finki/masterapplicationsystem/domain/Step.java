package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
public class Step {

    @Id
    private final String id = UUID.randomUUID().toString();

    private OffsetDateTime created;
    private OffsetDateTime closed;
    private int orderNumber;
    private String name;

    @ManyToOne
    private Process process;
}
