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

    public MasterTopic(
            Step step,
            String topic,
            String description,
            Document application,
            Document mentorApproval,
            Document biography,
            Document supplement) {

        super(step.getOrderNumber(), step.getName());
        this.topic = topic;
        this.description = description;
        this.application = application;
        this.mentorApproval = mentorApproval;
        this.biography = biography;
        this.supplement = supplement;
    }

    public MasterTopic() {
        super();
    }
}
