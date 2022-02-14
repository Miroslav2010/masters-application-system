package mk.ukim.finki.masterapplicationsystem.service;

import mk.ukim.finki.masterapplicationsystem.domain.Person;
import mk.ukim.finki.masterapplicationsystem.domain.dto.PersonDto;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.Role;

import java.util.List;

public interface PersonService {
    List<Person> getAll();

    Person getPerson(String id);

    List<Person> getAllPersonsFromRole(Role role);

    Person getSystemUser();

    void deletePerson(String id);

    Person createPerson(PersonDto person);

    Person getLoggedInUser();

    List<Person> getStudentServiceMembers();
    List<Person> getAllSecretaries();
    List<Person> getAllNNKMembers();
}
