package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.Data;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
public class Process {

    @Id
    private final String id = UUID.randomUUID().toString();

    @OneToOne
    private Master master;

    @Enumerated(EnumType.STRING)
    private ProcessState processState;

    public Process() {
        this.processState = ProcessState.APPLICATION;
    }
}
