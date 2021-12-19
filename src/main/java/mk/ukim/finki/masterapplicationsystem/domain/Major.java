package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class Major {

    @Id
    private final String id = UUID.randomUUID().toString();

    private String name;

    public Major(String name) {
        this.name = name;
    }
}
