package com.tienda.bicicletas.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Tienda de Bicicletas - SENA",
                version = "1.0",
                description = "Sistema para la gestión de inventario de bicicletas, desarrollado como proyecto para el SENA. " +
                        "Permite realizar operaciones CRUD completas y gestión de stock.",
                contact = @Contact(
                        name = "Juan Gabriel Londoño - Carlos Andres Mendez",
                        email = "juangabriellondonoarias@gmail.com" +
                                "camendez25@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        )
)
public class OpenApiConfig {
}