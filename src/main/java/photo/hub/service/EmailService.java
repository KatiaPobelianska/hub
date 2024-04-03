package photo.hub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import photo.hub.dto.EmailDto;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(EmailDto emailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDto.getEmail());
        message.setSubject(emailDto.getSubject());
        message.setText(emailDto.getText());
       javaMailSender.send(message);

    }
}
