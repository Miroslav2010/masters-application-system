package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Person;
import mk.ukim.finki.masterapplicationsystem.domain.dto.PersonDto;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.Role;
import mk.ukim.finki.masterapplicationsystem.domain.exceptions.InvalidPersonIdException;
import mk.ukim.finki.masterapplicationsystem.domain.mapper.PersonMapper;
import mk.ukim.finki.masterapplicationsystem.repository.PersonRepository;
import mk.ukim.finki.masterapplicationsystem.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public List<Person> getAllPersonsFromRole(Role role) {
        return personRepository.findAllByRolesContaining(role);
    }

    @Override
    public Person getSystemUser() {
        return personRepository.findAllByRolesContaining(Role.SYSTEM_USER).stream().findFirst()
                .orElseThrow(() -> new RuntimeException("System user not found"));
    }

    @Override
    public void deletePerson(String id) {
        personRepository.deleteById(id);
    }

    @Override
    public Person createPerson(PersonDto person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        Person p = this.personRepository.save(personMapper.toDomain(person));
        logger.info("Created new person with id: {} and name: {}", p.getId(), p.getFullName());
        return p;
    }

    @Override
    public Person getLoggedInUser() {
        return (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public List<Person> getStudentServiceMembers() {
        return personRepository.findAllByRolesContaining(Role.STUDENT_SERVICE);
    }

    @Override
    public List<Person> getAllSecretaries() {
        return personRepository.findAllByRolesContaining(Role.SECRETARY);
    }

    @Override
    public List<Person> getAllNNKMembers() {
        return personRepository.findAllByRolesContaining(Role.NNK);
    }
}
