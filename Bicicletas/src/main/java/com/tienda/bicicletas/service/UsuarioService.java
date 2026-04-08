package com.tienda.bicicletas.service;

import com.tienda.bicicletas.dto.request.UsuarioRequestDTO;
import com.tienda.bicicletas.dto.response.UsuarioResponseDTO;
import com.tienda.bicicletas.entity.Rol;
import com.tienda.bicicletas.entity.Usuario;
import com.tienda.bicicletas.mapper.UsuarioMapper;
import com.tienda.bicicletas.repository.RolRepository;
import com.tienda.bicicletas.repository.UsuarioRepository;
import com.tienda.bicicletas.repository.VentaRepository;
import com.tienda.bicicletas.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private RolRepository rolRepository; // Necesario para guardar usuarios con rol

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService; // Necesario para enviar el correo

    public List<UsuarioResponseDTO> listarTodos(){
        return usuarioRepository.findAll().stream()
                .filter(u -> u.getActivo() == null || u.getActivo())
                .map(usuarioMapper::toResposeDTO)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioResponseDTO> buscarPorId(Integer id){
        return usuarioRepository.findById(id).map(usuarioMapper::toResposeDTO);
    }

    @Transactional
    public UsuarioResponseDTO guardar(UsuarioRequestDTO request){
        Usuario entidad = usuarioMapper.toEntity(request);

        // Asignamos el rol manualmente
        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        entidad.getRoles().add(rol);

        entidad.setPassword(passwordEncoder.encode(request.getPassword()));
        entidad.setActivo(true);

        return usuarioMapper.toResposeDTO(usuarioRepository.save(entidad));
    }

    @Transactional
    public void eliminar(Integer id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // BORRADO INTELIGENTE:
        // 1. ¿Tiene facturas?
        boolean tieneVentas = ventaRepository.existsByClienteIdUsuario(id);
        // 2. ¿Tiene movimientos de inventario? (Admin/Vendedor)
        boolean tieneMovimientos = movimientoRepository.existsByUsuarioIdUsuario(id);

        if (tieneVentas || tieneMovimientos) {
            // Si tiene historia, solo desactivamos (Soft Delete)
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
        } else {
            // Si está "limpio", lo borramos físicamente
            usuarioRepository.delete(usuario);
        }
    }

    // --- ESTE ES EL MÉTODO QUE LE FALTABA A TU ARCHIVO ---
    @Transactional
    public void procesarRecuperacionPassword(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));

        String token = UUID.randomUUID().toString();
        usuario.setResetToken(token);
        usuario.setResetTokenExpiry(LocalDateTime.now().plusHours(1));

        // Guardamos y forzamos el envío a la DB
        usuarioRepository.saveAndFlush(usuario);

        // Enviamos el correo
        emailService.enviarCorreoRecuperacion(usuario.getEmail(), token);
    }

    @Transactional
    public Optional<UsuarioResponseDTO> actualizar(Integer id, UsuarioRequestDTO request){
        return usuarioRepository.findById(id)
                .map(existente -> {
                    usuarioMapper.updateEntityFromDto(request, existente);

                    if (request.getIdRol() != null) {
                        Rol nuevoRol = rolRepository.findById(request.getIdRol())
                                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
                        existente.getRoles().clear();
                        existente.getRoles().add(nuevoRol);
                    }

                    if(request.getPassword() != null && !request.getPassword().isBlank()){
                        existente.setPassword(passwordEncoder.encode(request.getPassword()));
                    }
                    return usuarioMapper.toResposeDTO(usuarioRepository.save(existente));
                });
    }
}