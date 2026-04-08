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
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        // Si la ruta empieza por /api/auth/, NO se aplica este filtro
        return path.startsWith("/api/auth/");
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Si no hay token, dejamos pasar a la siguiente etapa (Spring Security decidirá si rebota o no)
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

                // Mapeo de roles para que coincida con tu SecurityConfig
                String nombreRol = (rolId == 1) ? "ROLE_ADMIN" : "ROLE_CLIENTE";
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(nombreRol));

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Guardar atributos para uso en controladores si es necesario
                request.setAttribute("userId", userId);
                request.setAttribute("rolId", rolId);
            } else {
                // Si el token no es válido, enviamos 401 y cortamos la cadena
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token expirado o inválido");
                return;
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return; // IMPORTANTE: Cortar ejecución aquí
        }

        // ⚠️ SOLO UN doFilter AL FINAL para las peticiones exitosas o sin token
        filterChain.doFilter(request, response);
    }

    /*@Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/") || path.contains("swagger") || path.contains("v3/api-docs");
    }*/
}