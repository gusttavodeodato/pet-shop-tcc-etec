package dev.deodato.tcc.petshop.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.deodato.tcc.petshop.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Component
public class TokenConfig {

    @Value("${jwt.secret}")
    private String tokenSecret;


    public String generateToken(Usuario usuario) {
        Algorithm algorithm = Algorithm.HMAC256(tokenSecret);
        return JWT.create()
                .withClaim("usuarioId", usuario.getId())
                .withSubject(usuario.getEmail())
                .withExpiresAt(Instant.now().plus(Duration.ofDays(365)))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public Optional<JWTUsuarioData> validateToken(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(tokenSecret);
            DecodedJWT decode = JWT.require(algorithm)
                    .build().verify(token);

            return Optional.of(JWTUsuarioData.builder()
                    .usuarioId(decode.getClaim("usuarioId").asLong())
                    .email(decode.getSubject())
                    .build());

        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
    }
}
