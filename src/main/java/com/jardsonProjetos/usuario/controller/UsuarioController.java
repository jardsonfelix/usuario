package com.jardsonProjetos.usuario.controller;

import com.jardsonProjetos.usuario.business.UsuarioService;
import com.jardsonProjetos.usuario.business.dto.EnderecoDTO;
import com.jardsonProjetos.usuario.business.dto.TelefoneDTO;
import com.jardsonProjetos.usuario.business.dto.UsuarioDTO;
import com.jardsonProjetos.usuario.infrastructure.entity.Usuario;
import com.jardsonProjetos.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvarUsuario(@RequestBody UsuarioDTO usuarioDTO){
        return ResponseEntity.ok(usuarioService.salvarUsuario(usuarioDTO));
    }

    @PostMapping("/login")
    public String login(@RequestBody UsuarioDTO usuarioTDO){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioTDO.getEmail(),
                        usuarioTDO.getSenha())
        );
        return "Bearer " + jwtUtil.generateToken(authentication.getName());

    }

    @GetMapping
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorEmail(@RequestParam ("email") String email){
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email){
        usuarioService.deletaUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@RequestBody UsuarioDTO dto,
                                                       @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.atualizadoDadosUsuario(token, dto));
    }

    @PutMapping("/endereco")
    public ResponseEntity<EnderecoDTO> atualizarEndereco(@RequestBody EnderecoDTO dto,
                                                         @RequestParam ("id") Long id){
        return ResponseEntity.ok(usuarioService.atualizarEndereco(id,dto));
    }

    @PutMapping("/telefone")
    public ResponseEntity<TelefoneDTO> atualizarTelefone(@RequestBody TelefoneDTO dto,
                                                         @RequestParam ("id") Long id){
        return ResponseEntity.ok(usuarioService.atualizarTelefone(id,dto));
    }

    @PostMapping("/endereco")
    public ResponseEntity<EnderecoDTO> cadastraEndereco(@RequestBody EnderecoDTO dto,
                                                        @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.cadastrarEndereco(token,dto));
    }

    @PostMapping("/telefone")
    public ResponseEntity<TelefoneDTO> cadastraTelefone(@RequestBody TelefoneDTO dto,
                                                        @RequestHeader ("Authorization") String token){
        return ResponseEntity.ok(usuarioService.cadastroTelefone(token, dto));
    }


}
