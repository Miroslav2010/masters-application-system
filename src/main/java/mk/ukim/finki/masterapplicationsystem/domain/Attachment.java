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

    public Attachment(OffsetDateTime created, int orderNumber, String name, Document document) {
        super(created, orderNumber, name);
        this.document = document;
    }
}
