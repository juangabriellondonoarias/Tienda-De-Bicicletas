package com.tienda.bicicletas.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDTO {

    @NotBlank(message = "El documento es obligatorio")
    @Size(max = 20, message = "El documento no puede exceder los 20 caracteres")
    private String documento;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe proporcionar un formato de correo válido")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "El correo debe contener '@' y un dominio con punto (ejemplo@correo.com)"
    )
    private String email;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 30, message = "El nombre no puede exceder los 30 caracteres")
    private String nombre;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 10, message = "El teléfono no puede exceder los 10 caracteres")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "El teléfono debe tener exactamente 10 dígitos numéricos"
    )
    private String telefono;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    private Integer idRol;
}