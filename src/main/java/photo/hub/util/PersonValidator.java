package photo.hub.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import photo.hub.dto.PersonDto;
import photo.hub.model.Person;
import photo.hub.service.PersonDetailsService;

@Component
public class PersonValidator implements Validator {
    private final PersonDetailsService personDetailsService;

    @Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return PersonDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PersonDto personDto = (PersonDto) target;
        if (personDetailsService.getByEmail(personDto.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "user with such email is already present");
        }
        if (personDetailsService.getByUsername(personDto.getUsername()).isPresent()) {
            errors.rejectValue("username", "", "user with such name is already present");
        }

    }
}
