package com.upao.induct3d.backend.repository;

import java.time.LocalDateTime;

public interface IEmailService {

    //Estructura del email
    void sendEmail(String[] toUser, String subject, String message);
    void sendPasswordChangeNotification(String toUser, LocalDateTime changeDateTime);
}
