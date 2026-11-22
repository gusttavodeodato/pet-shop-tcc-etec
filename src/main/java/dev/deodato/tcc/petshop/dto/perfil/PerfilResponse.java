package dev.deodato.tcc.petshop.dto.perfil;

import java.time.LocalDate;

public record PerfilResponse(
        String nome,
        String email,
        String telefone,
        String endereco,
        String funcao,
        LocalDate membroDesde,
        Preferencias preferencias,
        Estatisticas estatisticas
) {

    public record Preferencias(boolean notificacoesEmail, boolean modoEscuro) {}

    public record Estatisticas(long agendamentos, long clientes, long pets, long itensEmEstoque) {}
}
