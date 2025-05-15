package com.upao.induct3d.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements IEmailService{

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String[] toUser, String subject, String message) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        String emailUser = "";
        mailMessage.setFrom(emailUser); //El que envia el correo (Induct3D)
        mailMessage.setTo(toUser); //Correos a los que se les va a enviar el email
        mailMessage.setSubject(subject); //Asunto del correo
        mailMessage.setText(message); //Cuerpo del correo a enviar

        mailSender.send(mailMessage);
    }
}
