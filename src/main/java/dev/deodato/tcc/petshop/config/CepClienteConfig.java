package dev.deodato.tcc.petshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class CepClienteConfig {

    @Bean
    public RestClient viaCepCliente(@Value("${cep.api.url}") String url) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(5000);

        return RestClient.builder()
                .baseUrl(url)
                .requestFactory(factory)
                .build();
    }
}
