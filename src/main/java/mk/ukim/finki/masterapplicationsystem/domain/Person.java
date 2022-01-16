package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.Data;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Person {

    @Id
    private final String id = UUID.randomUUID().toString();
    private String fullName;

    @ElementCollection(targetClass= Role.class)
    @Enumerated(EnumType.STRING)
    private final List<Role> roles = new ArrayList<>();
}
