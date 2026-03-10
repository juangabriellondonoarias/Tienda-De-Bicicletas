package com.tienda.bicicletas.security;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (jwtService.isTokenValid(token)) {
                String email = jwtService.extractEmail(token);
                Integer userId = jwtService.extractUserId(token);
                Integer rolId = jwtService.extractRolId(token);

                // 1. Mapear el ID del rol a la autoridad de Spring
                // IMPORTANTE: Asegúrate de que coincida con "ROLE_ADMIN" en tu SecurityConfig
                String nombreRol = (rolId == 1) ? "ROLE_ADMIN" : "ROLE_CLIENTE";
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(nombreRol));

                // 2. Crear el objeto de autenticación
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        authorities
                );

                // 3. ESTABLECER LA AUTENTICACIÓN EN EL CONTEXTO (Esto quita el 403)
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 4. Guardar atributos extra para uso interno si lo deseas
                request.setAttribute("email", email);
                request.setAttribute("userId", userId);
                request.setAttribute("rolId", rolId);

                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token expirado o inválido");
            }
        } catch (Exception e) {
            // Limpiar el contexto si algo falla para mayor seguridad
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/") || path.contains("swagger") || path.contains("v3/api-docs");
    }
}