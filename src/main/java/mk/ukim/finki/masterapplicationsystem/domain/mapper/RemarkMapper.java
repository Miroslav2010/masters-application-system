package mk.ukim.finki.masterapplicationsystem.domain.mapper;

import mk.ukim.finki.masterapplicationsystem.domain.Person;
import mk.ukim.finki.masterapplicationsystem.domain.Remark;
import mk.ukim.finki.masterapplicationsystem.domain.Role;
import mk.ukim.finki.masterapplicationsystem.domain.dto.PersonDto;
import mk.ukim.finki.masterapplicationsystem.domain.dto.RemarkDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RemarkMapper {

    PersonMapper INSTANCE = Mappers.getMapper( PersonMapper.class );

    Remark remarkDtoToRemark(RemarkDto remarkDto);
}
