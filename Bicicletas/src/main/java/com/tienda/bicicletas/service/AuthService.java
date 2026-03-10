package com.tienda.bicicletas.service;

import com.tienda.bicicletas.dto.auth.*;
import com.tienda.bicicletas.dto.request.UsuarioRequestDTO;
import com.tienda.bicicletas.entity.*;
import com.tienda.bicicletas.repository.*;
import com.tienda.bicicletas.security.JwtService;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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
}