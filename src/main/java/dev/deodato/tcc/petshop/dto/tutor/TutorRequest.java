package dev.deodato.tcc.petshop.dto.tutor;

import dev.deodato.tcc.petshop.model.Tutor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record TutorRequest(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
        String nome,

        @Size(max = 30, message = "O telefone deve ter no máximo 30 carateres.")
        String telefone,

        @Email(message = "E-mail digitado inválido.")
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
