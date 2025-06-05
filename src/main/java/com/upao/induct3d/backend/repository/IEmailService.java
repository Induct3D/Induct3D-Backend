package com.upao.induct3d.backend.repository;

public interface IEmailService {

    //Estructura del email
    void sendEmail(String[] toUser, String subject, String message);
}
