package com.tienda.bicicletas.service;

import com.tienda.bicicletas.dto.auth.*;
import com.tienda.bicicletas.dto.request.UsuarioRequestDTO;
import com.tienda.bicicletas.entity.*;
import com.tienda.bicicletas.repository.*;
import com.tienda.bicicletas.security.JwtService;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JavaMailSender mailSender;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String emailRemitente;

    // ── Registro ──────────────────────────────────────────────────────────────

    public String registrar(UsuarioRequestDTO dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email ya registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .documento(dto.getDocumento())
                .telefono(dto.getTelefono())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        Integer idRol = (dto.getIdRol() != null) ? dto.getIdRol() : 2; // 2 = ROLE_CLIENTE
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        usuario.getRoles().add(rol);
        usuarioRepository.save(usuario);
        return "Usuario registrado correctamente";
    }

    // ── Login ─────────────────────────────────────────────────────────────────

    public LoginResponseDTO login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        Integer idRol = usuario.getRoles().stream()
                .findFirst()
                .map(Rol::getIdRol)
                .orElse(2);

        String token = jwtService.generateToken(usuario.getIdUsuario(), idRol, usuario.getEmail());

        return new LoginResponseDTO(token, "Bearer", usuario.getIdUsuario(), idRol, usuario.getEmail());
    }

    // ── Recuperar contraseña ──────────────────────────────────────────────────

    public String forgotPassword(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No existe una cuenta con ese correo"));

        // Generar token único con expiración de 30 minutos
        String token = UUID.randomUUID().toString();
        usuario.setResetToken(token);
        usuario.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));
        usuarioRepository.save(usuario);

        // Enviar email con el link de reset
        String link = frontendUrl + "/reset-password?token=" + token;
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(emailRemitente);
        mensaje.setTo(email);
        mensaje.setSubject("🔑 Recuperación de contraseña - BikeStore");
        mensaje.setText(
                "Hola " + usuario.getNombre() + ",\n\n" +
                "Recibimos una solicitud para restablecer tu contraseña en BikeStore.\n\n" +
                "Haz clic en el siguiente enlace para crear una nueva contraseña:\n" +
                link + "\n\n" +
                "Este enlace expira en 30 minutos.\n\n" +
                "Si no solicitaste este cambio, ignora este mensaje.\n\n" +
                "── El equipo de BikeStore"
        );
        mailSender.send(mensaje);

        return "Correo de recuperación enviado correctamente";
    }

    // ── Restablecer contraseña ────────────────────────────────────────────────

    public String resetPassword(String token, String nuevaPassword) {
        Usuario usuario = usuarioRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido o ya utilizado"));

        if (usuario.getResetTokenExpiry() == null ||
            LocalDateTime.now().isAfter(usuario.getResetTokenExpiry())) {
            throw new RuntimeException("El enlace de recuperación ha expirado. Solicita uno nuevo.");
        }

        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuario.setResetToken(null);
        usuario.setResetTokenExpiry(null);
        usuarioRepository.save(usuario);

        return "Contraseña actualizada correctamente";
    }
}