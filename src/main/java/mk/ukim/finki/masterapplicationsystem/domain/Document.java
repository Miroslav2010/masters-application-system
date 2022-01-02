package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
public class Document {

    @Id
    private final String id = UUID.randomUUID().toString();

    private OffsetDateTime dateUploaded;

    private String location;
}
