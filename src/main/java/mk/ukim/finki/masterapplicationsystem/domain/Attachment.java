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
}
