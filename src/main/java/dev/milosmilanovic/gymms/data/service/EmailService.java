package dev.milosmilanovic.gymms.data.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@NoArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void send(String to, String message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(message, true);
            helper.setTo(to);
            helper.setSubject("You've received a gift code.");
            // TODO: Replace hard coded value for email
            helper.setFrom("milanovic32@hotmail.com", "Gym Management System - TEST");

            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send e-mail");
        }
    }

    public String constructGiftCodeMessage(long code, LocalDate expires) {
        String expiresStr = expires.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return String.format("<p style=\"font-size:1.2rem;\">Hello<br><br>You have received a gift code. Details are:<br>Code: %d<br>Expires: %s</p>", code, expiresStr);
    }

}
