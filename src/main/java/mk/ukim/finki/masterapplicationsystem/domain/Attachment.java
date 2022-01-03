package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.time.OffsetDateTime;

@Entity
@NoArgsConstructor
public class Attachment extends Step {

    @OneToOne
    private Document document;

    public Attachment(Step step, Document document) {
        super(step.getOrderNumber(), step.getName());
        this.document = document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
