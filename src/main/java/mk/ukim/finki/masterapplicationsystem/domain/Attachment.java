package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Attachment extends Step {

    @OneToOne
    private Document document;

    public Attachment(Step step, Document document) {
        super(step.getOrderNumber(), step.getName());
        this.document = document;
    }

    public Attachment(Step step) {
        super(step.getOrderNumber(), step.getName());
    }

    public Attachment() {

    }
}
