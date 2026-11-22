package dev.deodato.tcc.petshop.service;

import dev.deodato.tcc.petshop.config.UsuarioLogado;
import dev.deodato.tcc.petshop.dto.perfil.AlterarSenhaRequest;
import dev.deodato.tcc.petshop.dto.perfil.AtualizarPerfilRequest;
import dev.deodato.tcc.petshop.dto.perfil.PerfilResponse;
import dev.deodato.tcc.petshop.dto.perfil.PreferenciasRequest;
import dev.deodato.tcc.petshop.exception.PetShopException;
import dev.deodato.tcc.petshop.model.Usuario;
import dev.deodato.tcc.petshop.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PerfilService {

    private static final String FUNCAO = "Administrador";

    private final UsuarioLogado usuarioLogado;
    private final UsuarioRepository usuarios;
    private final PasswordEncoder passwordEncoder;
    private final TokenRecuperacaoRepository tokens;
    private final AgendamentoRepository agendamentos;
    private final TutorRepository tutores;
    private final PetRepository pets;
    private final ProdutoRepository produtos;

    public PerfilService(UsuarioLogado usuarioLogado, UsuarioRepository usuarios, PasswordEncoder passwordEncoder,
                         TokenRecuperacaoRepository tokens, AgendamentoRepository agendamentos, TutorRepository tutores,
                         PetRepository  pets, ProdutoRepository produtos) {
        this.usuarioLogado = usuarioLogado;
        this.usuarios = usuarios;
        this.passwordEncoder = passwordEncoder;
        this.tokens = tokens;
        this.agendamentos = agendamentos;
        this.tutores = tutores;
        this.pets = pets;
        this.produtos = produtos;
    }

    @Transactional(readOnly = true)
    public PerfilResponse obter() {
        return montar(usuarioLogado.obter());
    }

    @Transactional
    public PerfilResponse atualizar(AtualizarPerfilRequest request) {
        Usuario usuario = usuarioLogado.obter();
        usuario.setNome(request.nome().trim());
        usuario.setTelefone(vazioParaNulo(request.telefone()));
        usuario.setEndereco(vazioParaNulo(request.endereco()));
        usuarios.save(usuario);
        return montar(usuario);
    }

    @Transactional
    public void alterarSenha(AlterarSenhaRequest request) {
        if(!request.novaSenha().equals(request.confirmacao())) {
            throw new PetShopException("A confirmação não confere com a nova senha.");
        }

        Usuario usuario = usuarioLogado.obter();

        if(!passwordEncoder.matches(request.senhaAtual(), usuario.getPassword())) {
            throw new PetShopException("Senha atual incorreta.");
        }

        if(passwordEncoder.matches(request.novaSenha(), usuario.getPassword())) {
            throw new PetShopException("A nova senha deve ser diferente da atual.");
        }

        usuario.setSenha(passwordEncoder.encode(request.novaSenha()));
        usuarios.save(usuario);
        tokens.deleteByUsuarioId(usuario.getId());
    }

    @Transactional
    public PerfilResponse.Preferencias atualizarPreferencias(PreferenciasRequest request) {
        Usuario usuario = usuarioLogado.obter();

        if(request.notificacoesEmail() != null) {
            usuario.setNotificacoesEmail(request.notificacoesEmail());
        }

        if(request.modoEscuro() != null) {
            usuario.setModoEscuro(request.modoEscuro());
        }

        usuarios.save(usuario);
        return new PerfilResponse.Preferencias(usuario.isNotificacoesEmail(), usuario.isModoEscuro());
    }



    private PerfilResponse montar(Usuario usuario) {
        Long itensEmEstoque = produtos.somarSaldos();

        return new PerfilResponse(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getEndereco(),
                FUNCAO,
                usuario.getCriadoEm() != null ? usuario.getCriadoEm().toLocalDate() : null,
                new PerfilResponse.Preferencias(usuario.isNotificacoesEmail(), usuario.isModoEscuro()),
                new PerfilResponse.Estatisticas(
                        agendamentos.count(),
                        tutores.count(),
                        pets.count(),
                        itensEmEstoque == null ? 0 : itensEmEstoque));
    }

    private String vazioParaNulo(String texto) {
        return(texto == null || texto.isBlank()) ? null : texto.trim();
    }
}
