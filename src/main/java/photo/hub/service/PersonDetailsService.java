package photo.hub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import photo.hub.model.Person;
import photo.hub.repository.PersonRepository;
import photo.hub.security.PersonDetails;

import java.util.List;
import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByUsername(username);
        if (person.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new PersonDetails(person.get());
    }
    public Optional<Person> getById(long id){
        return personRepository.findById(id).stream().findAny();
    }

    public Person addAdmin(long id) {
        Optional<Person> person = getById(id);
        if (person.isEmpty()){
            throw new UsernameNotFoundException("user not found");
        }
        Person personToSave = person.get();
        personToSave.setRole("ROLE_ADMIN");
        return personRepository.save(personToSave);
    }
    public List<Person> getAllNotAdmin(){
        return personRepository.findAllByRoleNotContaining("ROLE_ADMIN");
    }
    public Optional<Person> getByEmail(String email){
        return personRepository.findByEmail(email);
    }
    public Optional<Person> getByUsername(String username){
        return personRepository.findByUsername(username);
    }
    public Person save(Person person){
        return personRepository.save(person);
    }
    public void activate(String username){
        Person person = getByUsername(username).orElseThrow();
        person.setEnable(true);
        save(person);
    }
}
