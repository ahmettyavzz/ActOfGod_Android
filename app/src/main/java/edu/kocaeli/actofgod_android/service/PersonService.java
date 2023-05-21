package edu.kocaeli.actofgod_android.service;

import edu.kocaeli.actofgod_android.model.Person;
import edu.kocaeli.actofgod_android.model.TcNoValidateDto;

public class PersonService {

    public Person validateToPerson(TcNoValidateDto dto, String androidId) {
        Person person = new Person();
        person.setTcNo(dto.getTcNo());
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setBirthYear(dto.getBirthYear());
        person.setAndroidId(androidId);
        return person;
    }
}
