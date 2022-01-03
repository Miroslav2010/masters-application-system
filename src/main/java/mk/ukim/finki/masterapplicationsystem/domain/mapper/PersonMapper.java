package mk.ukim.finki.masterapplicationsystem.domain.mapper;

import mk.ukim.finki.masterapplicationsystem.domain.*;
import mk.ukim.finki.masterapplicationsystem.domain.dto.PersonDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper( PersonMapper.class );

    /**
     * Maps dto person object to domain model.
     *
     * @param personDto dto object.
     * @return mapped domain model {@link Person}.
     */
    default Person toDomain(PersonDto personDto) {
        if (personDto.getRoles().contains(Role.PROFESSOR)) {
            return toProfessorDomain(personDto);
        } else if (personDto.getRoles().contains(Role.STUDENT)) {
            return toStudentDomain(personDto);
        } else if (personDto.getRoles().contains(Role.SECRETARY)) {
            return toSecretaryDomain(personDto);
        } else if (personDto.getRoles().contains(Role.STUDENT_SERVICE)) {
            return toStudentServiceDomain(personDto);
        }
        return toPersonDomain(personDto);
    }

    /**
     * Maps dto person object to domain model.
     *
     * @param personDto dto object.
     * @return mapped domain model {@link Person}.
     */
    Person toPersonDomain(PersonDto personDto);

    /**
     * Maps dto person object to domain model.
     *
     * @param personDto dto object.
     * @return mapped domain model {@link Professor}.
     */
    Professor toProfessorDomain(PersonDto personDto);

    /**
     * Maps dto person object to domain model.
     *
     * @param personDto dto object.
     * @return mapped domain model {@link Student}.
     */
    Student toStudentDomain(PersonDto personDto);

    /**
     * Maps dto person object to domain model.
     *
     * @param personDto dto object.
     * @return mapped domain model {@link Secretary}.
     */
    Secretary toSecretaryDomain(PersonDto personDto);

    /**
     * Maps dto person object to domain model.
     *
     * @param personDto dto object.
     * @return mapped domain model {@link StudentService}.
     */
    StudentService toStudentServiceDomain(PersonDto personDto);
}
