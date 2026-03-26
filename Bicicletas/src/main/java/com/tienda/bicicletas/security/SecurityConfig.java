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
import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtValidationFilter jwtValidationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // 1. Activar CORS con la configuración que creamos abajo
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. RUTAS PÚBLICAS (Login, Registro y Swagger)
                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // 2. VITAL: Permitir OPTIONS en TODAS las rutas para evitar errores de CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 3. BICICLETAS: Ver (GET) es público, modificar (POST, PUT, DELETE) es solo ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/bicicletas/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/bicicletas/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/bicicletas/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/bicicletas/**").hasAuthority("ROLE_ADMIN")

                        // 4. VENTAS: Cliente compra, Admin ve todo
                        .requestMatchers(HttpMethod.POST, "/api/ventas/cliente/**").hasAuthority("ROLE_CLIENTE")
                        .requestMatchers("/api/ventas/**").hasAuthority("ROLE_ADMIN")

                        // 5. USUARIOS Y MOVIMIENTOS: Solo Administrador
                        .requestMatchers("/api/usuarios/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/movimientos/**").hasAuthority("ROLE_ADMIN")

                        // Cualquier otra ruta requiere estar logueado
                        .anyRequest().authenticated()
                )
                // 2. Tu filtro de JWT
                .addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200",
                "https://frontend-tienda-bicicletas-ov8t.vercel.app/")); // Tu Angular
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}