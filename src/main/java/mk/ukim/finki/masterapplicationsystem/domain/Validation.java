package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class Validation extends Step {

    public Validation(Step step) {
        super(step.getOrderNumber(), step.getName());
    }
}
