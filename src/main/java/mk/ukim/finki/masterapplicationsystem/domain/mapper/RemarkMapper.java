package mk.ukim.finki.masterapplicationsystem.domain.mapper;

import mk.ukim.finki.masterapplicationsystem.domain.Remark;
import mk.ukim.finki.masterapplicationsystem.domain.dto.RemarkDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RemarkMapper {

    RemarkMapper INSTANCE = Mappers.getMapper( RemarkMapper.class );

    Remark remarkDtoToRemark(RemarkDto remarkDto);
}
