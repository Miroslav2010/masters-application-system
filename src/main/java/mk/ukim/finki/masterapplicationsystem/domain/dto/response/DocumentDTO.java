package mk.ukim.finki.masterapplicationsystem.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class DocumentDTO {

    private String id;

    private String name;

    private String location;

}
