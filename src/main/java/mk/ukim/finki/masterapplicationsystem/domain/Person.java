package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class Person {

    @Id
    private final String id = UUID.randomUUID().toString();
    private String fullName;

    @ElementCollection(targetClass=Role.class)
    @Enumerated(EnumType.STRING)
    private final List<Role> roles = new ArrayList<>();

    public Person(String fullName, List<Role> roles) {
        this.fullName = fullName;
        this.roles.addAll(roles);
    }
}
