package com.tienda.bicicletas.controller;

import com.tienda.bicicletas.dto.auth.LoginRequestDTO;
import com.tienda.bicicletas.dto.auth.LoginResponseDTO;
import com.tienda.bicicletas.dto.request.UsuarioRequestDTO;
import com.tienda.bicicletas.service.AuthService;
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

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Endpoints para el manejo de acceso, registro de usuarios y generación de tokens JWT")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Inicio de sesión",
            description = "Valida las credenciales del usuario (email y password) y retorna un token JWT junto con el perfil del usuario (ID y Rol)."
    )
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

    @Operation(
            summary = "Registro de nuevo usuario",
            description = "Crea un nuevo usuario en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos de registro inválidos o email ya existente"),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar el registro")
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.ok(authService.registrar(request));
    }
}