package dev.deodato.tcc.petshop.dto.tutor;

import dev.deodato.tcc.petshop.model.Tutor;

import java.time.LocalDateTime;

public record TutorResponse(
        Long id,
        String nome,
        String telefone,
        String email,
        LocalDateTime criadoEm
) {
    public static TutorResponse fromEntity(Tutor tutor) {
        return new TutorResponse(
                tutor.getId(),
                tutor.getNome(),
                tutor.getTelefone(),
                tutor.getEmail(),
                tutor.getCriadoEm()
        );
    }
}
