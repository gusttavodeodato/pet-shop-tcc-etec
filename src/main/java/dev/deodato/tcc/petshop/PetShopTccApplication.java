package dev.deodato.tcc.petshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PetShopTccApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetShopTccApplication.class, args);
	}

}
