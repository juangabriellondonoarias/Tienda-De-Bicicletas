package com.tienda.bicicletas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public void enviarCorreoRecuperacion(String emailDestino, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDestino);
        message.setSubject("Restablecer Contraseña - Tienda de Bicicletas");

        // Creamos el link que el usuario verá en su correo
        String link = frontendUrl + "/reset-password?token=" + token;

        message.setText("Hola,\n\nHemos recibido una solicitud para restablecer tu contraseña.\n" +
                "Haz clic en el siguiente enlace para continuar:\n\n" + link +
                "\n\nEste enlace expirará en 15 minutos.\nSi no fuiste tú, ignora este mensaje.");

        mailSender.send(message);
    }
}