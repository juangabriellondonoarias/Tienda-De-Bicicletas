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

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        // PUBLICOS
                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // IMPORTANTE PARA CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // BICICLETAS
                        .requestMatchers(HttpMethod.GET, "/api/bicicletas/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/bicicletas/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/bicicletas/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/bicicletas/**").hasAuthority("ROLE_ADMIN")

                        // VENTAS
                        .requestMatchers("/api/ventas/**")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENTE")

                        // USUARIOS
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/{id}")
                        .authenticated()

                        .requestMatchers("/api/usuarios/**")
                        .hasAuthority("ROLE_ADMIN")

                        // MOVIMIENTOS
                        .requestMatchers("/api/movimientos/**")
                        .hasAuthority("ROLE_ADMIN")

                        .anyRequest().authenticated()
                );

        // JWT FILTER
        http.addFilterBefore(
                jwtValidationFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:4200",
                "http://localhost:5173",
                "https://frontend-tienda-bicicletas-s3b8-zeta.vercel.app",
                "https://*.vercel.app"
        ));

        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));

        configuration.setAllowCredentials(true);

        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}