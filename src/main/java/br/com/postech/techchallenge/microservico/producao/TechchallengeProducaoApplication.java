package br.com.postech.techchallenge.microservico.producao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TechchallengeProducaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechchallengeProducaoApplication.class, args);
	}
}
