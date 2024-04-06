package photo.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import photo.hub.service.PersonDetailsService;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final PersonDetailsService personDetailsService;

    @Autowired
    public AdminController(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }
    @GetMapping("/allNotAdmin")
    public ResponseEntity<?> getAllNotAdmin(){
        return new ResponseEntity<>(personDetailsService.getAllNotAdmin(), HttpStatus.OK);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<?> addAdmin(@PathVariable("id") long id){
        try {
            personDetailsService.addAdmin(id);
            return new ResponseEntity<>(HttpStatus.OK);

        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
