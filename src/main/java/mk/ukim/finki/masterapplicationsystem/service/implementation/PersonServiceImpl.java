package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.*;
import mk.ukim.finki.masterapplicationsystem.domain.dto.PersonDto;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.Role;
import mk.ukim.finki.masterapplicationsystem.domain.exceptions.InvalidPersonIdException;
import mk.ukim.finki.masterapplicationsystem.domain.mapper.PersonMapper;
import mk.ukim.finki.masterapplicationsystem.repository.PersonRepository;
import mk.ukim.finki.masterapplicationsystem.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);
    private final PasswordEncoder passwordEncoder;

    public PersonServiceImpl(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
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
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        Person p = this.personRepository.save(personMapper.toDomain(person));
        logger.info("Created ne person with id: %s and name: %s",p.getId(),p.getFullName());
        return p;
    }
}
