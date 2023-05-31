package edu.kocaeli.actofgod_android.service;

import edu.kocaeli.actofgod_android.model.PersonDto;
import edu.kocaeli.actofgod_android.model.TcNoValidateDto;

public class PersonService {

    public PersonDto validateToPerson(TcNoValidateDto dto, String androidId) {
        PersonDto personDto = new PersonDto();
        personDto.setTcNo(dto.getTcNo());
        personDto.setFirstName(dto.getFirstName());
        personDto.setLastName(dto.getLastName());
        personDto.setBirthYear(dto.getBirthYear());
        personDto.setAndroidId(androidId);
        return personDto;
    }
}
