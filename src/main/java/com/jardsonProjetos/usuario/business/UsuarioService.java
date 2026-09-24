package com.jardsonProjetos.usuario.business;

import com.jardsonProjetos.usuario.business.converter.UsuarioCoverter;
import com.jardsonProjetos.usuario.business.dto.UsuarioDTO;
import com.jardsonProjetos.usuario.infrastructure.entity.Usuario;
import com.jardsonProjetos.usuario.infrastructure.exceptions.ConflictException;
import com.jardsonProjetos.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.jardsonProjetos.usuario.infrastructure.repository.UsuarioRepository;
import com.jardsonProjetos.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioCoverter usuarioCoverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioDTO salvarUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioCoverter.paraUsuario(usuarioDTO);
        return usuarioCoverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificarEmailExistente(email);
            if (existe == true) {
                throw new ConflictException("Email já cadastrado " + email);
            }
        } catch (ConflictException e) {
            throw new ConflictException("Email já cadastrado " + e.getCause());
        }
    }

    public boolean verificarEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email não encontrado" + email));
    }

    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);

    }

    public UsuarioDTO atualizadoDadosUsuario(String token, UsuarioDTO dto){
       //buscou email do usuário pelo token para tirar a obrigatoriedade do email
        String email = jwtUtil.extrairEmailToken(token.substring(7));
      dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha() ): null);
        //buscou os dados do usuario no banco de dados
       Usuario usarioEntity = usuarioRepository.findByEmail(email).orElseThrow(()->
               //mesclou os dados  que recebemos na requisição DTO com os dados do banco de dados
               new ResourceNotFoundException("Email não localizado "));
       Usuario usuario =usuarioCoverter.updateUsuario(dto, usarioEntity);
        //salvou os dados do usuário convertido e depois pegou e converteu para UsuárioDTO
       return usuarioCoverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }
}
