package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Validation extends Step {

    public Validation(Step step) {
        super(step);
    }

    public Validation() {

    }
}
