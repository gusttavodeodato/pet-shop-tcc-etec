package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.exception.PetShopException;
import dev.deodato.tcc.petshop.model.TokenRecuperacaoSenha;
import dev.deodato.tcc.petshop.model.Usuario;
import dev.deodato.tcc.petshop.repository.TokenRecuperacaoRepository;
import dev.deodato.tcc.petshop.repository.UsuarioRepository;
import dev.deodato.tcc.petshop.util.Emails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Optional;

@Service
public class RecuperacaoSenhaService {

    private static final int VALIDADE_MINUTOS = 30;
    private static final int INTERVALO_MINUTOS = 1;
    private static final SecureRandom ALEATORIO = new SecureRandom();

    private final UsuarioRepository usuarios;
    private final TokenRecuperacaoRepository tokens;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final String frontendUrl;

    public RecuperacaoSenhaService(UsuarioRepository usuarios, TokenRecuperacaoRepository tokens, EmailService emailService, PasswordEncoder passwordEncoder,
                                   @Value("${app.frontend.url}") String frontendUrl) {
        this.usuarios = usuarios;
        this.tokens = tokens;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.frontendUrl = frontendUrl;
    }

    @Transactional
    public void solicitar(String email) {
        Optional<Usuario> encontrado = usuarios.findUsuarioByEmail(Emails.normalizar(email));
        if(encontrado.isEmpty()) {
            return;
        }
        Usuario usuario = encontrado.get();

        LocalDateTime agora = LocalDateTime.now();
        if(tokens.existsByUsuarioIdAndCriadoEmAfter(usuario.getId(), agora.minusMinutes(INTERVALO_MINUTOS))) {
            return;
        }

        tokens.deleteByUsuarioId(usuario.getId());

        String token = gerarToken();

        TokenRecuperacaoSenha registro = new TokenRecuperacaoSenha();
        registro.setHash(hash(token));
        registro.setUsuario(usuario);
        registro.setDataExpiracao(agora.plusMinutes(VALIDADE_MINUTOS));
        tokens.save(registro);

        emailService.enviarRecuperacaoSenha(usuario.getEmail(), frontendUrl + "/redefinir-senha?token=" + token);
    }

    @Transactional
    public void redefinir(String token, String novaSenha) {
        TokenRecuperacaoSenha registro = tokens.findByHash(hash(token))
                .orElseThrow(() -> new PetShopException("Link inválido ou expirado."));

        if(registro.isUsado() || registro.getDataExpiracao().isBefore(LocalDateTime.now())) {
            throw new PetShopException("Link inválido ou expirado.");
        }

        Usuario usuario = registro.getUsuario();
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarios.save(usuario);

        registro.setUsado(true);
        tokens.save(registro);
    }

    private String gerarToken() {
        byte[] bytes = new byte[32];
        ALEATORIO.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String token) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] resumo = sha.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(resumo);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
