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

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setApplication(Document application) {
        this.application = application;
    }

    public void setMentorApproval(Document mentorApproval) {
        this.mentorApproval = mentorApproval;
    }

    public void setBiography(Document biography) {
        this.biography = biography;
    }

    public void setSupplement(Document supplement) {
        this.supplement = supplement;
    }
}
