package photo.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import photo.hub.service.ActivationService;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/activation")
public class ActivationController {
    private final ActivationService activationService;

    @Autowired
    public ActivationController(ActivationService activationService) {
        this.activationService = activationService;
    }
    @GetMapping("/{key}")
    public ResponseEntity<?> activate(@PathVariable("key") String key){
        try {
            activationService.activate(key);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
