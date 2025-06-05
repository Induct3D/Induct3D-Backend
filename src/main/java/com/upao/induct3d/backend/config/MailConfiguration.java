package com.upao.induct3d.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfiguration {

    @Value("${email.sender}")
    private String emailUser;

    @Value("${email.password}")
    private String emailPassword;

    @Bean
    public JavaMailSender getJavaMailSender() {
        //Objeto que va a configurar el envío de los emails
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(emailUser);
        mailSender.setPassword(emailPassword);

        //Se obtiene las propiedades del mailSender
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp"); //Se indica cuál se usará el smtp para enviar el correo electrónico
        props.put("mail.smtp.auth", "true"); //Se está habilitando la autenticación con el usuario y la contraseña
        props.put("mail.smtp.starttls.enable", "true"); //Se habilita el cifrado entre la comunicación del proveedor
        props.put("mail.debug", "true"); //Se imprime la info del email

        return mailSender;
    }
}
