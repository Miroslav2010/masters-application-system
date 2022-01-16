package mk.ukim.finki.masterapplicationsystem.domain.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class MasterDto {

    private String id;

    private OffsetDateTime dateTime;

    private OffsetDateTime finishedDate;

    private OffsetDateTime masterDefenseDate;

    private String archiveNumber;
}
