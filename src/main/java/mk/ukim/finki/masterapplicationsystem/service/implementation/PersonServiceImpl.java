package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.*;
import mk.ukim.finki.masterapplicationsystem.domain.dto.PersonDto;
import mk.ukim.finki.masterapplicationsystem.domain.exceptions.InvalidPersonIdException;
import mk.ukim.finki.masterapplicationsystem.domain.mapper.PersonMapper;
import mk.ukim.finki.masterapplicationsystem.repository.PersonRepository;
import mk.ukim.finki.masterapplicationsystem.service.PersonService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
        this.personMapper = PersonMapper.INSTANCE;
    }

    @Override
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    @Override
    public Person getPerson(String id) {
        return personRepository.findById(id).orElseThrow(InvalidPersonIdException::new);
    }

    @Override
    public List<Person> getProfessors() {
        return personRepository.findAllByRolesContaining(Role.PROFESSOR);
    }

    @Override
    public void deletePerson(String id) {
        personRepository.deleteById(id);
    }

    @Override
    public Person createPerson(PersonDto person) {
        return this.personRepository.save(personMapper.toDomain(person));
    }
}