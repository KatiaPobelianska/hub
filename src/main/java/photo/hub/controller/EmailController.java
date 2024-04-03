package photo.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import photo.hub.dto.EmailDto;
import photo.hub.service.EmailService;

import javax.validation.Valid;

@RestController
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }
    @PostMapping
    public ResponseEntity<?> sendEmail(@RequestBody @Valid EmailDto emailDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        emailService.sendEmail(emailDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
