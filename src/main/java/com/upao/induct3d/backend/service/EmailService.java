package com.upao.induct3d.backend.service;

import com.upao.induct3d.backend.repository.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {

    @Value("${email.sender}")
    private String emailUser;

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String[] toUser, String subject, String message) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(emailUser);
            helper.setTo(toUser);
            helper.setSubject(subject);
            helper.setText(getStyledHtml(message), true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage());
        }
    }

    private String getStyledHtml(String code) {
        return """
            <div style="font-family: 'Segoe UI', sans-serif; background-color: #ffffff; padding: 24px; border-radius: 8px; border: 1px solid #e5e7eb;">
                <h2 style="color: #7A1C1C; margin-bottom: 12px;">Recuperación de contraseña</h2>
                <p style="font-size: 16px; color: #111827;">Hola,</p>
                <p style="font-size: 16px; color: #111827;">
                    Tu código de verificación es:
                </p>
                <div style="font-size: 32px; font-weight: bold; margin: 20px 0; color: #7A1C1C;">%s</div>
                <p style="font-size: 14px; color: #6b7280;">
                    Este código expirará en 10 minutos. Si no solicitaste este código, ignora este mensaje.
                </p>
                <hr style="margin-top: 24px; border: none; border-top: 1px solid #e5e7eb;" />
                <p style="font-size: 12px; color: #9ca3af; margin-top: 12px;">
                    Induct3D · Seguridad Inteligente para tus Proyectos
                </p>
            </div>
        """.formatted(code);
    }
}
