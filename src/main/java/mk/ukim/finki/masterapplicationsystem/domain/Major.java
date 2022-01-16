package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class Major {

    @Id
    private final String id = UUID.randomUUID().toString();

    private String name;

    public Major(String name) {
        this.name = name;
    }

    public Major() {

    }
}
