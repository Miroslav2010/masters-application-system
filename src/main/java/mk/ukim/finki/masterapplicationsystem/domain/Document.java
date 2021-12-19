package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class Document {

    @Id
    private final String id = UUID.randomUUID().toString();

    private OffsetDateTime dateUploaded;

    private String location;

    public Document(OffsetDateTime dateUploaded, String location) {
        this.dateUploaded = dateUploaded;
        this.location = location;
    }
}
