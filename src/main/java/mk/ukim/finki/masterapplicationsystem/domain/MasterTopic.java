package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.time.OffsetDateTime;

@Entity
@NoArgsConstructor
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
            OffsetDateTime created,
            int orderNumber,
            String name,
            String topic,
            String description,
            Document application,
            Document mentorApproval,
            Document biography,
            Document supplement) {

        super(created, orderNumber, name);
        this.topic = topic;
        this.description = description;
        this.application = application;
        this.mentorApproval = mentorApproval;
        this.biography = biography;
        this.supplement = supplement;
    }
}
