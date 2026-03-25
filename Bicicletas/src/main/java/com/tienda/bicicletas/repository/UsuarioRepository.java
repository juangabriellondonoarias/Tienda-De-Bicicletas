package com.tienda.bicicletas.repository;

import com.tienda.bicicletas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByDocumento(String documento);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByResetToken(String resetToken);
}
