package dev.deodato.tcc.petshop.controller;

import dev.deodato.tcc.petshop.config.TokenConfig;
import dev.deodato.tcc.petshop.dto.usuario.UsuarioLoginRequest;
import dev.deodato.tcc.petshop.dto.usuario.UsuarioLoginResponse;
import dev.deodato.tcc.petshop.dto.usuario.UsuarioRegisterRequest;
import dev.deodato.tcc.petshop.dto.usuario.UsuarioRegisterResponse;
import dev.deodato.tcc.petshop.model.Usuario;
import dev.deodato.tcc.petshop.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public AuthController(UsuarioRepository usuarioRepository,  PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenConfig tokenConfig) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioLoginResponse> login(@Valid @RequestBody UsuarioLoginRequest request) {

        // AUTENTICA ESSE USUARIO USANDO UM EMAIL E UMA SENHA
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
        Authentication authenticate = authenticationManager.authenticate(token);

        Usuario usuario = (Usuario) authenticate.getPrincipal();
        String tokenSecret = tokenConfig.generateToken(usuario);
        return ResponseEntity.ok(new UsuarioLoginResponse(tokenSecret));
    }


    @PostMapping("/registrar")
    public ResponseEntity<UsuarioRegisterResponse> registrar(@Valid @RequestBody UsuarioRegisterRequest request) {
        Usuario usuario = new Usuario();
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setEmail(request.email());
        usuarioRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioRegisterResponse(usuario.getEmail()));
    }
}
