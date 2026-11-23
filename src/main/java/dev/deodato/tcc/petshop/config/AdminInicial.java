package dev.deodato.tcc.petshop.config;

import dev.deodato.tcc.petshop.model.Usuario;
import dev.deodato.tcc.petshop.repository.UsuarioRepository;
import dev.deodato.tcc.petshop.util.Emails;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AdminInicial implements ApplicationRunner {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder passwordEncoder;
    private final String email;
    private final String senha;

    public AdminInicial(UsuarioRepository usuarios, PasswordEncoder passwordEncoder, @Value("${ADMIN_EMAIL:}") String email, @Value("${ADMIN_SENHA:}") String senha) {
        this.usuarios = usuarios;
        this.passwordEncoder = passwordEncoder;
        this.email = email;
        this.senha = senha;
    }

    @Override
    public void run(ApplicationArguments args) {
        if(usuarios.count() > 0) {
            return;
        }

        if(email.isBlank() || senha.length() < 8) {
            log.warn("Nenhum usuário cadastrado. Defina ADMIN_EMAIL e ADMIN_SENHA (mínimo de 8 caracteres) para criar o administrador inicial.");
            return;
        }

        Usuario admin = new Usuario();
        admin.setEmail(Emails.normalizar(email));
        admin.setSenha(passwordEncoder.encode(senha));
        admin.setNome("Administrador");
        usuarios.save(admin);

        log.info("Administrador inicial criado {}", admin.getEmail());
    }
}
