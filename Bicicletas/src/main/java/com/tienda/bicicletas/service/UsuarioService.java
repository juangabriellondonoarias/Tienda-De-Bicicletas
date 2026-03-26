package com.tienda.bicicletas.service;

import com.tienda.bicicletas.dto.request.UsuarioRequestDTO;
import com.tienda.bicicletas.dto.response.UsuarioResponseDTO;
import com.tienda.bicicletas.entity.Usuario;
import com.tienda.bicicletas.mapper.UsuarioMapper;
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
    private PasswordEncoder passwordEncoder; // Inyectamos el encriptador

    public List<UsuarioResponseDTO> listarTodos(){
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResposeDTO)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioResponseDTO> buscarPorId(Integer id){
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toResposeDTO);
    }

    public UsuarioResponseDTO guardar(UsuarioRequestDTO request){
        Usuario entidad = usuarioMapper.toEntity(request);

        // Encriptamos la contraseña antes de guardar
        entidad.setPassword(passwordEncoder.encode(request.getPassword()));

        return usuarioMapper.toResposeDTO(usuarioRepository.save(entidad));
    }

    public void eliminar(Integer id){
        usuarioRepository.deleteById(id);
    }

    public Optional<UsuarioResponseDTO> actualizar(Integer id, UsuarioRequestDTO request){
        return usuarioRepository.findById(id)
                .map(existente -> {
                    usuarioMapper.updateEntityFromDto(request, existente);

                    // si el request trae una nueva contraseña, la encriptamos
                    if(request.getPassword() != null && !request.getPassword().isBlank()){
                        existente.setPassword(passwordEncoder.encode(request.getPassword()));
                    }
                    return usuarioMapper.toResposeDTO(usuarioRepository.save(existente));
                });
    }
}
