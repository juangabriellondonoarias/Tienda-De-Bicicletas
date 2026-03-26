package com.tienda.bicicletas.controller;

import com.tienda.bicicletas.dto.auth.LoginRequestDTO;
import com.tienda.bicicletas.dto.auth.LoginResponseDTO;
import com.tienda.bicicletas.dto.request.UsuarioRequestDTO;
import com.tienda.bicicletas.service.AuthService;
import com.tienda.bicicletas.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Endpoints para el manejo de acceso, registro de usuarios y generación de tokens JWT")
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    @Operation(summary = "Inicio de sesión",
            description = "Valida las credenciales del usuario y retorna un token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }


    @Operation(summary = "Registro de nuevo usuario",
            description = "Crea un nuevo usuario en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o email ya existente")
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.ok(authService.registrar(request));
    }

    @Operation(summary = "Solicitar recuperación de contraseña",
            description = "Envía un correo con un enlace único para restablecer la contraseña.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correo enviado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe cuenta con ese correo")
    })

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        // Llamamos al servicio para que genere el token, lo guarde y envíe el correo
        usuarioService.procesarRecuperacionPassword(email);
        return ResponseEntity.ok(Map.of("message", "Correo enviado con éxito"));
    }

    @Operation(summary = "Restablecer contraseña",
            description = "Valida el token recibido por email y actualiza la contraseña del usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contraseña actualizada"),
            @ApiResponse(responseCode = "400", description = "Token inválido o expirado")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String nuevaPassword = body.get("newPassword");
        return ResponseEntity.ok(authService.resetPassword(token, nuevaPassword));
    }
}