package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
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

    private String name;

    public Document(OffsetDateTime dateUploaded, String location,String name) {
        this.dateUploaded = dateUploaded;
        this.location = location;
        this.name=name;
    }

    public Document() {

    }
}
