package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class MasterTopic extends Step {

    private String topic;

    private String description;

    @OneToOne
    private Document application;

    @OneToOne
    private Document mentorApproval;

    @OneToOne
    private Document biography;

    @OneToOne
    private Document supplement;
}
