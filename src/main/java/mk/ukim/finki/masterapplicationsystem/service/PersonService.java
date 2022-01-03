package mk.ukim.finki.masterapplicationsystem.service;

import mk.ukim.finki.masterapplicationsystem.domain.Person;
import mk.ukim.finki.masterapplicationsystem.domain.dto.PersonDto;

import java.util.List;

public interface PersonService {
    List<Person> getAll();
    Person getPerson(String id);
    List<Person> getProfessors();
    void deletePerson(String id);
    Person createPerson(PersonDto person);
}
