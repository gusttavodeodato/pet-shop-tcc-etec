package dev.deodato.tcc.petshop.dto.tutor;

import dev.deodato.tcc.petshop.model.Tutor;


public record TutorRequest(
        String nome,
        String telefone,
        String email
) {
    public Tutor toEntity() {
        Tutor tutor = new Tutor();
        preencher(tutor);
        return tutor;
    }

    public void preencher (Tutor tutor) {
        tutor.setNome(nome);
        tutor.setTelefone(telefone);
        tutor.setEmail(email);
    }
}
