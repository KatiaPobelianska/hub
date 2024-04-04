package photo.hub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import photo.hub.dto.EmailDto;
import photo.hub.dto.PersonDto;
import photo.hub.model.Person;

@Service
public class RegistrationService {
    private final PersonDetailsService personDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ActivationService activationService;

    @Autowired
    public RegistrationService(PersonDetailsService personDetailsService, PasswordEncoder passwordEncoder, EmailService emailService, ActivationService activationService) {
        this.personDetailsService = personDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.activationService = activationService;
    }

    public Person register(PersonDto personDto) {
        personDto.setPassword(passwordEncoder.encode(personDto.getPassword()));
        personDto.setRole("ROLE_USER");
        Person person = convertPersonDtoToPerson(personDto);
        person.setEnable(false);
        Person personSaved = personDetailsService.save(person);
        String key = activationService.generateCode(person.getUsername());
        emailService.sendEmail(new EmailDto(personDto.getEmail(), "Account activation",
                "To activate your account, please, follow this link:\n" +
                        "http://localhost:80/activation/" + key));
        return personSaved;

    }

    private Person convertPersonDtoToPerson(PersonDto personDto) {
        Person person = new Person();
        person.setId(personDto.getId());
        person.setUsername(personDto.getUsername());
        person.setEmail(personDto.getEmail());
        person.setPassword(personDto.getPassword());
        person.setRole(personDto.getRole());
        return person;
    }

}
