package dev.deodato.tcc.petshop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String remetente;

    public EmailService(JavaMailSender mailSender, @Value("${spring.mail.username}") String remetente) {
        this.mailSender = mailSender;
        this.remetente = remetente;
    }

    @Async
    public void enviarRecuperacaoSenha(String destino, String link) {
        try{
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setFrom(remetente);
            mensagem.setTo(destino);
            mensagem.setSubject("Recuperação de senha - Petshop");
            mensagem.setText("Olá!\n\n"
                        + "Recebemos um pedido para redefinir a sua senha. "
                        + "Clique no link abaixo (válido por 30 minutos): \n\n"
                        + link + "\n\n"
                        + "Se você não fez esse pedido, ignore este e-mail.");
            mailSender.send(mensagem);
        } catch (MailException e) {
            log.error("Falha ao enviar e-mail de recuperação", e);
        }
    }
}
