package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class StudentService extends Person {
}
