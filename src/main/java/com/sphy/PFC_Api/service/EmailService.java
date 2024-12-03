package com.sphy.PFC_Api.service;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final String username = "personalfuelcontrolapp@gmail.com";
    //private final String password = "cnwt zage bjch yjtk";
    private final String password = "cnwtzagebjchyjtk";

    public void sendWelcomeEmail(String toEmail, String userName) throws MessagingException {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Bienvenido a nuestra aplicación PFC-App");
        message.setText("Hola " + userName.toUpperCase() + ",\n\nBienvenido a nuestra aplicación. Personal Fuel Controller . \n" +  "-".repeat(60) +  "\nEstamos muy contentos de tenerte con nosotros.\n"
        + "Recuerda que gracias a nuestra aplicación podrás hacer un uso más eficiente y económico de tus vehículos.\n\n\n" +
        "Todo el equipo de diseño, desarrollo, producción y marketing de PFC-App te deseamos una ....FELIZ NAVIDAD  ;D  OH-OH-OH");

        Transport.send(message);
    }
}
