package com.tienda.bicicletas.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtValidationFilter jwtValidationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. RUTAS PÚBLICAS: Login, Registro y Documentación
                        .requestMatchers("/api/auth/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()

                        // --- CAMBIOS AQUÍ: REGLAS ESPECÍFICAS DE VENTAS ---

                        // Solo el ADMIN/VENDEDOR puede registrar ventas por la ruta de vendedor
                        .requestMatchers(HttpMethod.POST, "/api/ventas/vendedor/**").hasAuthority("ROLE_ADMIN")

                        // El CLIENTE (y Admin) solo puede usar la ruta de cliente
                        .requestMatchers(HttpMethod.POST, "/api/ventas/cliente/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENTE")

                        // Ver el historial de ventas (GET) disponible para ambos
                        .requestMatchers(HttpMethod.GET, "/api/ventas/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENTE")

                        // ------------------------------------------------

                        // 2. PERMISOS DE BICICLETAS
                        .requestMatchers(HttpMethod.GET, "/api/bicicletas/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/api/bicicletas/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/bicicletas/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/bicicletas/**").hasAuthority("ROLE_ADMIN")

                        // 3. GESTIÓN DE MOVIMIENTOS Y USUARIOS (Solo Admin)
                        .requestMatchers("/api/movimientos/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/usuarios/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/detalles-ventas/**").hasAuthority("ROLE_ADMIN")

                        // 4. CUALQUIER OTRA RUTA: Requiere Admin por seguridad
                        .anyRequest().hasAuthority("ROLE_ADMIN")
                )
                .addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}