package com.tienda.bicicletas.service;

import com.tienda.bicicletas.dto.request.UsuarioRequestDTO;
import com.tienda.bicicletas.dto.response.UsuarioResponseDTO;
import com.tienda.bicicletas.entity.Rol;
import com.tienda.bicicletas.entity.Usuario;
import com.tienda.bicicletas.mapper.UsuarioMapper;
import com.tienda.bicicletas.repository.RolRepository;
import com.tienda.bicicletas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inyectamos el encriptador

    public List<UsuarioResponseDTO> listarTodos(){
        return usuarioRepository.findAll().stream()
                .filter(u -> u.getActivo() == null || u.getActivo())
                .map(usuarioMapper::toResposeDTO)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioResponseDTO> buscarPorId(Integer id){
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toResposeDTO);
    }

    public UsuarioResponseDTO guardar(UsuarioRequestDTO request) {
        Usuario entidad = usuarioMapper.toEntity(request);

        // 🔔 SOLUCIÓN: Buscar el rol manualmente y asignarlo a la entidad
        // ya que el Mapper lo tiene como "ignore = true"
        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        entidad.getRoles().add(rol);
        entidad.setPassword(passwordEncoder.encode(request.getPassword()));

        return usuarioMapper.toResposeDTO(usuarioRepository.save(entidad));
    }

    public void eliminar(Integer id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setActivo(false); // 👈 Lo marcamos como inactivo
        usuarioRepository.save(usuario); // Guardamos el cambio, NO lo borramos
    }

    public Optional<UsuarioResponseDTO> actualizar(Integer id, UsuarioRequestDTO request){
        return usuarioRepository.findById(id)
                .map(existente -> {
                    usuarioMapper.updateEntityFromDto(request, existente);

                    // 🚀 BUSCAMOS Y ASIGNAMOS EL ROL MANUALMENTE
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
