package mk.ukim.finki.masterapplicationsystem.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
@AllArgsConstructor
public class MasterPreviewListDTO {

    private List<MasterPreviewDTO> masterPreviews;

    private Long mastersNumber;
}
