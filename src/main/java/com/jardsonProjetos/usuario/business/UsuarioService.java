package com.jardsonProjetos.usuario.business;

import com.jardsonProjetos.usuario.business.converter.UsuarioCoverter;
import com.jardsonProjetos.usuario.business.dto.UsuarioDTO;
import com.jardsonProjetos.usuario.infrastructure.entity.Usuario;
import com.jardsonProjetos.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioCoverter usuarioCoverter;

        public UsuarioDTO salvarUsuario(UsuarioDTO usuarioDTO){
            Usuario usuario = usuarioCoverter.paraUsuario(usuarioDTO);
            return usuarioCoverter.paraUsuarioDTO(usuarioRepository.save(usuario));
        }
}
