package photo.hub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import photo.hub.dto.PersonDto;
import photo.hub.model.Person;

@Service
public class RegistrationService {
    private final PersonDetailsService personDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(PersonDetailsService personDetailsService, PasswordEncoder passwordEncoder) {
        this.personDetailsService = personDetailsService;
        this.passwordEncoder = passwordEncoder;
    }
    public Person register(PersonDto personDto){
        personDto.setPassword(passwordEncoder.encode(personDto.getPassword()));
        personDto.setRole("ROLE_USER");
        Person person = convertPersonDtoToPerson(personDto);
        return personDetailsService.save(person);
    }
    private Person convertPersonDtoToPerson(PersonDto personDto){
        Person person = new Person();
        person.setId(personDto.getId());
        person.setUsername(personDto.getUsername());
        person.setEmail(personDto.getEmail());
        person.setPassword(personDto.getPassword());
        person.setRole(personDto.getRole());
        return person;
    }
}
