package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Student extends Person {

    private String index;

    public Student(String fullName, List<Role> roles, String index) {
        super(fullName, roles);
        this.index = index;
    }
}
