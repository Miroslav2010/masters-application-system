package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
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
