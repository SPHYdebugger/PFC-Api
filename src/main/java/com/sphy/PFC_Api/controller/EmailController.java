package com.sphy.PFC_Api.controller;

import com.sphy.PFC_Api.model.EmailRequest;
import com.sphy.PFC_Api.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendWelcomeEmail(@RequestBody EmailRequest emailRequest) {
        try {
            System.out.println("petición de envío de correo");
            emailService.sendWelcomeEmail(emailRequest.getEmail(), emailRequest.getUserName());
            return ResponseEntity.ok("Correo de bienvenida enviado con éxito.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo.");
        }
    }
}
