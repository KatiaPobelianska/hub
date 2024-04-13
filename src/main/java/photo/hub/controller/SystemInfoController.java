package photo.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import photo.hub.model.SystemInfo;
import photo.hub.service.SystemInfoService;
import photo.hub.util.SystemInfoValidator;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/infos")
public class SystemInfoController {
    private final SystemInfoService systemInfoService;
    private final SystemInfoValidator validator;

    @Autowired
    public SystemInfoController(SystemInfoService systemInfoService, SystemInfoValidator validator) {
        this.systemInfoService = systemInfoService;
        this.validator = validator;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        try {
            return new ResponseEntity<>(systemInfoService.getById(id), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("system info with id: " + id + " not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        return new ResponseEntity<>(systemInfoService.getAll(), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid SystemInfo systemInfo, BindingResult bindingResult){
        validator.validate(systemInfo, bindingResult);
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(systemInfoService.save(systemInfo), HttpStatus.CREATED);
    }
    @GetMapping("/find")
    public ResponseEntity<?> getByTitle(@RequestParam("title") String title){
        try {
            return new ResponseEntity<>(systemInfoService.getByTitle(title), HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>("system info with title " + title + " not found",HttpStatus.NOT_FOUND);
        }
    }
    @PatchMapping("/{id}")
    public ResponseEntity<?> edit(@RequestBody SystemInfo systemInfo, @PathVariable("id") long id){
        try {
            return new ResponseEntity<>(systemInfoService.edit(systemInfo, id), HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>( "system info with id: " + id + "not found", HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id){
        systemInfoService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
